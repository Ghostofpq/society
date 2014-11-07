package com.gop.society.security;

import com.gop.society.exceptions.CustomInvalidLoginOrPasswordException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.User;
import com.gop.society.services.UserService;
import com.gop.society.utils.UserInfo;
import com.gop.society.utils.UserRole;
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
        log.debug("authenticate: {}", authentication.toString());
        final String login = authentication.getName();
        final User user;
        try {
            user = userService.getByLogin(login);
        } catch (CustomNotFoundException e) {
            log.error("1-Invalid login or Password");
            throw new CustomInvalidLoginOrPasswordException("Invalid login or Password");
        }

        if (authentication instanceof UserAuthenticationToken) {
            log.debug("Token is already a UserAuthenticationToken");
            final String encodedPassword = authentication.getCredentials().toString();
            if (userService.authenticateEncoded(user.getPassword(), encodedPassword)) {
                return authentication;
            }
            log.error("3-Invalid login or Password");
            throw new CustomInvalidLoginOrPasswordException("Invalid login or Password");
        } else {
            log.debug("1st connection, encoding password");
            final String encodedPassword = userService.encodePassword(authentication.getCredentials().toString(), user.getSalt());
            if (userService.authenticateEncoded(user.getPassword(), encodedPassword)) {
                List<GrantedAuthority> grantedAuths = new ArrayList<>();
                for (UserRole role : user.getUserRole()) {
                    grantedAuths.add(new SimpleGrantedAuthority(role.toString()));
                }
                final UserInfo userInfo = new UserInfo();
                userInfo.setId(user.getId());
                userInfo.setLogin(user.getLogin());
                userInfo.setEmail(user.getEmail());

                return new UserAuthenticationToken(authentication.getPrincipal(), encodedPassword, grantedAuths, userInfo);
            } else {
                log.error("2-Invalid login or Password");
                throw new CustomInvalidLoginOrPasswordException("Invalid login or Password");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.debug("supports : {}", authentication);
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
