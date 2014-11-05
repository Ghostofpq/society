package com.gop.society.portal;

import com.gop.society.models.User;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
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
            if (authentication.getPrincipal() instanceof User) {
                // authenticated, returning the user desc
                final User user = (User) authentication.getPrincipal();
                // Write the response
                resp.getWriter().write(mapper.writeValueAsString(user));
            } else {
                authentication.setAuthenticated(false);
                resp.getWriter().write(mapper.writeValueAsString(null));
            }
        }
    }
}
