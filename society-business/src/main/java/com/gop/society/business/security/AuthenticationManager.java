package com.gop.society.business.security;

import com.gop.society.exceptions.CustomInvalidLoginOrPasswordException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.User;
import com.gop.society.services.UserService;
import com.gop.society.utils.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private ShaPasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        log.info("AuthenticationManager started !");
        passwordEncoder = new ShaPasswordEncoder(256);
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        log.debug("authenticate({})", authentication.toString());
        User user;
        try {
            user = userService.getByLogin(authentication.getName());
        } catch (Exception e) {
            log.error("1-Invalid login");
            throw new CustomInvalidLoginOrPasswordException("Invalid login or Password");
        }

        final String encodedPassword = passwordEncoder.encodePassword(authentication.getCredentials().toString(), user.getSalt());
        if (user.getEncodedPassword().equals(encodedPassword)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            for (final UserRole role : user.getUserRoles()) {
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
        log.debug("supports({})", authentication);
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public String getAuthenticatedUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        } else {
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
                log.debug("current user id : {}", (String) usernamePasswordAuthenticationToken.getPrincipal());
                return (String) usernamePasswordAuthenticationToken.getPrincipal();
            } else {
                return null;
            }
        }
    }

    public User getAuthenticatedUser() throws CustomNotFoundException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("No security context !");
            throw new CustomNotFoundException("null");
        } else {
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
                return userService.get((String) usernamePasswordAuthenticationToken.getPrincipal());
            } else {
                log.error("Invalid security context !");
                throw new CustomNotFoundException("null");
            }
        }
    }

    public boolean isAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString()));
    }
}
