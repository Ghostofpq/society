package com.gop.society.security;

import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Organisation;
import com.gop.society.services.OrganisationService;
import com.gop.society.utils.CurrencyCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;


/**
 * @author GhostOfPQ
 */
@Slf4j
@Aspect
@Component("securityAspect")
@EnableAspectJAutoProxy
public class SecurityAspect {
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private OrganisationService organisationService;

    @PostConstruct
    private void init() {
        log.info("SecurityAspect Loaded !");
    }

    @Before("execution(* com.gop.society.controllers.UserController.get(..)) && args(id)")
    public void beforeGetUser(final String id) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeGetUser");
        final String currentUserId = customAuthenticationProvider.getAuthenticatedUserId();
        if (!id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", customAuthenticationProvider.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }

    @Before("execution(* com.gop.society.controllers.UserController.updateField(..)) && args(id,field,value)")
    public void beforeUpdateFieldUser(final String id, final String field, final Object value) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeUpdateFieldUser");
        final String currentUserId = customAuthenticationProvider.getAuthenticatedUserId();
        if (!id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", customAuthenticationProvider.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }

    @Before("execution(* com.gop.society.controllers.UserController.update(..)) && args(id,fields)")
    public void beforeUpdateUser(final String id, final Map<String, Object> fields) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeUpdateUser");
        final String currentUserId = customAuthenticationProvider.getAuthenticatedUserId();
        if (!id.equals(currentUserId)) {
            log.error("expected ID {}", id);
            log.error("current ID {}", customAuthenticationProvider.getAuthenticatedUserId());
            throw new CustomNotAuthorizedException();
        }
    }

    @Before("execution(* com.gop.society.controllers.UserController.delete(..)) && args(id)")
    public void beforeDelete(final String id) throws CustomNotAuthorizedException {
        log.debug("SecurityCheck : beforeUpdateUser");
        final String currentUserId = customAuthenticationProvider.getAuthenticatedUserId();
        if (!id.equals(currentUserId)) {
            throw new CustomNotAuthorizedException();
        }
    }

    @Before("execution(* com.gop.society.controllers.OrganisationController.createCurrency(..)) && args(id,currencyCreationRequest)")
    public void beforeCreateCurrency(final String id, final CurrencyCreationRequest currencyCreationRequest) throws CustomNotAuthorizedException, CustomNotFoundException {
        log.debug("SecurityCheck : beforeCreateCurrency");
        final String currentUserId = customAuthenticationProvider.getAuthenticatedUserId();
        final Organisation organisation = organisationService.get(id);
        if (!organisation.getManagers().contains(currentUserId)) {
            throw new CustomNotAuthorizedException();
        }
    }
}
