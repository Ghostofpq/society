package com.gop.society.portal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
        JSONObject res = new JSONObject();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // NOT authenticated
            res.put("authenticated", false);
        } else {
            // authenticated!
            res.put("authenticated", true);
            res.put("login", authentication.getName());

            // roles
            JSONArray roles = new JSONArray();
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                roles.add(auth.toString());
            }
            res.put("roles", roles);
        }

        // write response
        resp.getWriter().write(res.toJSONString());
    }
}
