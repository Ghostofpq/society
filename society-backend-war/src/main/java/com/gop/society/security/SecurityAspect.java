package com.gop.society.security;

import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @Before("execution(* com.gop.society.controllers.UserController.get(..)) && args(id)")
    public void beforeGetUser(final String id) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeGetUser");
        final String currentUserId = authenticationManager.getAuthenticatedUserId();
        if (!authenticationManager.isAdmin() && !id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", authenticationManager.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }

    @Before("execution(* com.gop.society.controllers.UserController.updateField(..)) && args(id,field,value)")
    public void beforeUpdateUser(final String id, final String field, final Object value) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeUpdateUser");
        final String currentUserId = authenticationManager.getAuthenticatedUserId();
        if (!authenticationManager.isAdmin() && !id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", authenticationManager.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }

    @Before("execution(* com.gop.society.controllers.UserController.updateField(..)) && args(id,fields)")
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
