package com.gop.society.portal.security;

import com.gop.society.exceptions.CustomInvalidLoginOrPasswordException;
import com.gop.society.models.User;
import com.gop.society.utils.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GhostOfPQ
 */


@Slf4j
@Component("authenticationManager")
public class AuthenticationManager implements AuthenticationProvider {
    private ShaPasswordEncoder passwordEncoder;
    @PostConstruct
    private void init() {
        log.info("AuthenticationManager started !");
        passwordEncoder = new ShaPasswordEncoder(256);
    }


    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        log.debug("authenticate: {}", authentication.toString());
        final String login = authentication.getName();
        final User user;
        RestTemplate userController = new RestTemplate();
        try {
            user = userController.getForObject("http://localhost:1337/api/users?id=" + login + "&by=login", User.class);
        } catch (Exception e) {
            log.error("1-Invalid login");
            throw new CustomInvalidLoginOrPasswordException("Invalid login or Password");
        }
        log.debug("1st connection, encoding password");
        final String encodedPassword = passwordEncoder.encodePassword(authentication.getCredentials().toString(), user.getSalt());
        if (user.getPassword().equals(encodedPassword)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            for (UserRole role : user.getUserRole()) {
                grantedAuths.add(new SimpleGrantedAuthority(role.toString()));
            }
            return new UsernamePasswordAuthenticationToken(user.getId(), authentication.getCredentials(), grantedAuths);
        } else {
            log.error("2-Invalid Password");
            throw new CustomInvalidLoginOrPasswordException("Invalid login or Password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.debug("supports : {}", authentication);
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
