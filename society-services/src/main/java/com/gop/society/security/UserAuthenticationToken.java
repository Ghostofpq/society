package com.gop.society.security;

import com.gop.society.utils.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author GhostOfPQ
 */
@Slf4j
public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private UserInfo userInfo;

    public UserAuthenticationToken(final Object principal, final Object credentials, final Collection<? extends GrantedAuthority> authorities, final UserInfo userInfo) {
        super(principal, credentials, authorities);
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
