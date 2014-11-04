package com.gop.society.utils;

import com.gop.society.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author GhostOfPQ
 */
@Slf4j
public class UserAuthenticationToken extends AbstractAuthenticationToken {

    private User loggedUser;

    public UserAuthenticationToken(final User user,
                                   final Collection<GrantedAuthority> grantedAuthorityCollection) {
        super(grantedAuthorityCollection);
        this.setAuthenticated(true);
        this.loggedUser = user;
        log.debug("New UserAuthenticationToken : {} / {}", user, grantedAuthorityCollection);
    }

    @Override
    public Object getPrincipal() {
        return loggedUser;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
