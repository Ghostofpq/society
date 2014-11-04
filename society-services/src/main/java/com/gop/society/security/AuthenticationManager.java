package com.gop.society.security;

import com.gop.society.exceptions.CustomInvalidLoginOrPasswordException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.User;
import com.gop.society.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GhostOfPQ
 */


@Slf4j
@Component("authenticationManager")
public class AuthenticationManager implements AuthenticationProvider {
    @Autowired
    private UserService userService;

    @PostConstruct
    private void init() {
        log.info("AuthenticationManager started !");
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String login = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final User user;

        try {
            user = userService.getByLogin(login);
        } catch (CustomNotFoundException e) {
            throw new CustomInvalidLoginOrPasswordException("Invalid login or Password");
        }

        String encodedPassword = userService.encodePassword(password);
        if (encodedPassword.equals(user.getPassword())) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            for (String role : user.getUserRole()) {
                grantedAuths.add(new SimpleGrantedAuthority(role));
            }
            return new UsernamePasswordAuthenticationToken(login, encodedPassword, grantedAuths);
        } else {
            throw new CustomInvalidLoginOrPasswordException("Invalid login or Password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
