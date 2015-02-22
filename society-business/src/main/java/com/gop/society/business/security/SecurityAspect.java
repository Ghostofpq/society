package com.gop.society.business.security;

import com.gop.society.exceptions.CustomNotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


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

    @Before("execution(* com.gop.society.business.controllers.UserController.updateField(..)) && args(id,field,value)")
    public void beforeUpdateUser(final String id, final String field, final Object value) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeUpdateUser");
        final String currentUserId = authenticationManager.getAuthenticatedUserId();
        if (!authenticationManager.isAdmin() && !id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", authenticationManager.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }

    @Before("execution(* com.gop.society.business.controllers.UserController.updateField(..)) && args(id,fields)")
    public void beforeUpdateUser(final String id, final Map<String, Object> fields) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeUpdateUser");
        final String currentUserId = authenticationManager.getAuthenticatedUserId();
        if (!authenticationManager.isAdmin() && !id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", authenticationManager.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }
}
