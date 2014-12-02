package com.gop.society.portal.servlets;

import com.gop.society.utils.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Http servlet that acts as a proxy: all incoming request
 * are forwarded to a remote URL.
 * <p/>
 * Init param "baseRemoteUrl" allow to configure the base URL for
 * remote calls (parsed by Spring using SpringContext)
 */
@Slf4j
public class ProxyServlet extends HttpServlet {
    // *************
    // static
    // *************
    private static final long serialVersionUID = 9871654661L;

    /**
     * Key for servlet init param to configure base URL of remote service.
     */
    public static final String INIT_PARAM_BASE_REMOTE_URL = "baseRemoteUrl";

    /**
     * Key to set a properties file to load from classpath with keys...
     */
    public static final String INIT_PARAM_PROPERTIES = "properties";
    /**
     * Key to define the key to load from Properties file (only if key "properties" is set)
     */
    public static final String INIT_PARAM_BASE_REMOTE_URL_PROPERTY = "baseRemoteUrlPropKey";

    public static final String INIT_PARAM_HEADERS = "headers";

    public static final int BUFFER_SIZE = 4 * 1024;

    // *************
    // instance
    // *************

    /**
     * Base URL of remote service.
     */
    private String baseRemoteUrl = "";

    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * Servlet initialization.
     *
     * @param config servlet configuration
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        String initParamProperties = config.getInitParameter(INIT_PARAM_PROPERTIES);
        String initParambaseRemotUrlPropKey = config.getInitParameter(INIT_PARAM_BASE_REMOTE_URL_PROPERTY);
        String initParamBaseRemoteUrl = config.getInitParameter(INIT_PARAM_BASE_REMOTE_URL);

        if (initParamProperties != null && initParambaseRemotUrlPropKey != null) {
            // => must load properties file and find baseUrl in it
            log.debug("init | baseURL from properties file  " + initParamProperties);
            Properties props = new Properties();
            try {
                InputStream in = this.getClass().getClassLoader().getResourceAsStream(initParamProperties);
                if (in == null) {
                    throw new IOException("properties file '" + initParamProperties + "' not found");
                }
                props.load(in);
                this.baseRemoteUrl = props.getProperty(initParambaseRemotUrlPropKey);
                log.debug("init | baseURL (from properties file) : " + this.baseRemoteUrl);
            } catch (IOException e) {
                // failed
                log.warn("failed to load baseUrl from properties file", e);
            }

        } else if (initParamBaseRemoteUrl != null) {
            // using baseUrl
            log.debug("init | baseURL : " + initParamBaseRemoteUrl);
            this.baseRemoteUrl = initParamBaseRemoteUrl;
        } else {
            log.debug("baseUrl not set from web.xml with init params...");
        }


        // init param : "headers"
        // format : <key>:<value>;<key>:<value
        String initParamHeaders = config.getInitParameter(INIT_PARAM_HEADERS);
        if (initParamHeaders != null) {
            // adding headers
            for (String s : initParamHeaders.split(",")) {
                String[] elements = s.split(";");
                if (elements.length > 1) {
                    log.debug("init | adding header {} : {}", elements[0], elements[1]);
                    this.headers.put(elements[0], elements[1]);
                }
            }
        }

    }

    /**
     * Translate a local URL to remote URL.
     *
     * @param request
     * @return
     * @throws java.net.MalformedURLException
     */
    public URL translateURL(HttpServletRequest request) throws MalformedURLException {
        String res = request.getPathInfo();
        String params = request.getQueryString();

        log.trace("request original URL : " + res);

        res = this.baseRemoteUrl + res;
        if (params != null && !params.isEmpty()) {
            res = res + "?" + params;
        }

        log.trace("new URL : " + res);

        return new URL(res);
    }

    /**
     * Handling incoming HTTP requests.
     *
     * @param request  incoming HTTP request
     * @param response HTTP reponse
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            URL url = translateURL(request);

            // Proxy
            doProxy(request, response, url);

        } catch (Exception e) {
            log.warn("error", e.getMessage());
            //response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Open HTTP Connection to real host, and proxifies the request.
     *
     * @param request
     * @param response
     * @param url
     * @throws java.io.IOException
     */
    public void doProxy(HttpServletRequest request, HttpServletResponse response, URL url) throws IOException {

        HttpURLConnection httpConn = null;

        try {

            // *** PROXY ***

            // HTTP Connection
            httpConn = (HttpURLConnection) url.openConnection();

            String httpMethod = request.getMethod();
            log.info(httpMethod);
            httpConn.setRequestMethod(httpMethod);
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setFollowRedirects(false);
            httpConn.setUseCaches(true);
            for (Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) {
                String headerName = e.nextElement().toString();
                if (!this.headers.containsKey(headerName)) {
                    httpConn.setRequestProperty(headerName, request.getHeader(headerName));
                    log.debug("{} : {}", headerName, request.getHeader(headerName));
                }
            }
            httpConn.setRequestProperty("currentUser", getAuthenticatedUser());
            log.debug("currentUser : {}", getAuthenticatedUser());
            httpConn.setRequestProperty("isAdmin", String.valueOf(isAdmin()));
            log.debug("isAdmin : {}", String.valueOf(isAdmin()));
            // custom headers
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                httpConn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            // connect...
            httpConn.connect();

            // POST > handle body
            //if (httpMethod.equals(HttpMethod.POST.name()) || httpMethod.equals(HttpMethod.PUT.name())) {
            if (request.getHeader("Content-Type") != null) {

                log.info("content type => copy content");

                // Copy content from request in ou request out.
                BufferedInputStream clientToProxyBuf = null;
                BufferedOutputStream proxyToWebBuf = null;
                try {
                    clientToProxyBuf = new BufferedInputStream(request.getInputStream());
                    proxyToWebBuf = new BufferedOutputStream(httpConn.getOutputStream());

                    // buffer size 4Ko
                    byte[] buff = new byte[BUFFER_SIZE];
                    int len;
                    while ((len = clientToProxyBuf.read(buff)) > 0) {
                        proxyToWebBuf.write(buff, 0, len);
                    }

                    proxyToWebBuf.flush();
                    proxyToWebBuf.close();
                    clientToProxyBuf.close();
                } catch (Exception e) {
                    ProxyServlet.log.warn("error", e);
                } finally {
                    if (clientToProxyBuf != null) {
                        clientToProxyBuf.close();
                    }
                    if (proxyToWebBuf != null) {
                        proxyToWebBuf.close();
                    }
                }
            } else {
                log.info("no need to copy content...");
            }

            // *** RESPONSE ***

            // Reponse Status Code
            response.setStatus(httpConn.getResponseCode());
            // Response Headers
            for (Iterator i = httpConn.getHeaderFields().entrySet().iterator(); i.hasNext(); ) {
                Map.Entry mapEntry = (Map.Entry) i.next();
                if (mapEntry.getKey() != null) {
                    response.setHeader(mapEntry.getKey().toString(), ((List) mapEntry.getValue()).get(0).toString());
                }
            }

            // Response Body
            BufferedInputStream webToProxyBuf = null;
            try {
                webToProxyBuf = new BufferedInputStream(httpConn.getInputStream());
            } catch (IOException e) {
                webToProxyBuf = new BufferedInputStream(httpConn.getErrorStream());
            }

            BufferedOutputStream proxyToClientBuf = new BufferedOutputStream(response.getOutputStream());
            // buffer size 4Ko
            byte[] buff = new byte[BUFFER_SIZE];
            int len;
            while ((len = webToProxyBuf.read(buff)) > 0) {
                proxyToClientBuf.write(buff, 0, len);
            }

            proxyToClientBuf.flush();
            proxyToClientBuf.close();
            webToProxyBuf.close();

            // Close http connection.
            httpConn.disconnect();

        } catch (FileNotFoundException e) {
            ProxyServlet.log.info("file not found => 404");
            response.reset();
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setHeader("Content-Type", "text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write("404 - not found");
            writer.flush();

        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
    }


    private String getAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        } else {
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
                return (String) usernamePasswordAuthenticationToken.getPrincipal();
            } else {
                return null;
            }
        }
    }

    private boolean isAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString()));
    }

}
