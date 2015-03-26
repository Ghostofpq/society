package com.gop.society.controllers;

import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.models.Currency;
import com.gop.society.models.Organisation;
import com.gop.society.security.CustomAuthenticationProvider;
import com.gop.society.services.AccountService;
import com.gop.society.services.CurrencyService;
import com.gop.society.services.OrganisationService;
import com.gop.society.services.UserService;
import com.gop.society.utils.AccountType;
import com.gop.society.utils.CurrencyCreationRequest;
import com.gop.society.utils.OrganisationCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/api/organisations")
@Component("organisationController")
public class OrganisationController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @PostConstruct
    private void init() {
        log.info("OrganisationController started !");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Organisation create(
            @RequestBody final OrganisationCreationRequest organisationCreationRequest)
            throws CustomBadRequestException {
        log.debug("create({})", organisationCreationRequest.toString());
        final Organisation organisation = new Organisation();
        organisation.setName(organisationCreationRequest.getName());
        organisation.setDescription(organisationCreationRequest.getDescription());
        organisation.addManager(customAuthenticationProvider.getAuthenticatedUserId());
        // Save
        return organisationService.add(organisation);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Organisation get(
            @PathVariable("id") final String id)
            throws CustomNotFoundException,
            CustomNotAuthorizedException {
        log.debug("get({})", id);
        return organisationService.get(id);
    }

    @RequestMapping(value = "/{id}/join", method = RequestMethod.GET)
    @ResponseBody
    public Organisation join(
            @PathVariable("id") final String id)
            throws CustomNotFoundException,
            CustomBadRequestException,
            CustomNotAuthorizedException {
        log.debug("join({})", id);
        Organisation organisation = organisationService.get(id);
        organisation.addMember(customAuthenticationProvider.getAuthenticatedUserId());
        return organisationService.update(organisation);
    }

    @RequestMapping(value = "/{id}/currency", method = RequestMethod.POST)
    @ResponseBody
    public Organisation createCurrency(
            @PathVariable("id") final String id,
            @RequestBody final CurrencyCreationRequest currencyCreationRequest)
            throws CustomNotFoundException,
            CustomBadRequestException,
            CustomNotAuthorizedException {
        log.debug("createCurrency({},{})", id, currencyCreationRequest);
        Organisation organisation = organisationService.get(id);

        //Create currency
        Currency currency = new Currency();
        currency.setName(currencyCreationRequest.getName());
        currency.setOwnerId(id);
        currency = currencyService.add(currency);

        //Create organisation account
        Account account = new Account();
        account.setAccountType(AccountType.ORGANISATION);
        account.setOwnerId(organisation.getId());
        account.setBalance(currencyCreationRequest.getInitialAmount());
        account.setCurrencyId(currency.getId());
        account = accountService.add(account);

        //Add account to organisation
        organisation.getAccounts().add(account.getId());
        return organisationService.update(organisation);
    }
}
