package com.gop.society.business.security;

import com.gop.society.exceptions.CustomNotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author GhostOfPQ
 */
@Slf4j
@Aspect
@Component("securityAspect")
public class SecurityAspect {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Before("execution(* com.gop.society.business.controllers.UserController.get(..)) && args(id)")
    public void beforeGetUser(final String id) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeGetUser");
        final String currentUserId = authenticationManager.getAuthenticatedUserId();
        if (!authenticationManager.isAdmin() && !id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", authenticationManager.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }

    @Before("execution(* com.gop.society.business.controllers.UserController.updateEmail(..)) && args(id,email)")
    public void beforeUpdateUser(final String id, final String email) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeUpdateUser");
        final String currentUserId = authenticationManager.getAuthenticatedUserId();
        if (!authenticationManager.isAdmin() && !id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", authenticationManager.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }
}
