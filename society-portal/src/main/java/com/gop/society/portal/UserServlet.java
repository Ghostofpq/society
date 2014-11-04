package com.gop.society.portal;

import com.gop.society.models.User;
import com.gop.society.utils.UserAuthenticationToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 97171654661L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final ObjectMapper mapper = new ObjectMapper();
        if (authentication == null || !authentication.isAuthenticated()) {
            // NOT authenticated
            resp.getWriter().write(mapper.writeValueAsString(null));
        } else {
            // authenticated, returning the user desc
            final UserAuthenticationToken token = (UserAuthenticationToken) authentication.getPrincipal();
            final User user = (User) token.getPrincipal();
            // Write the response
            resp.getWriter().write(mapper.writeValueAsString(user));
        }
    }
}
