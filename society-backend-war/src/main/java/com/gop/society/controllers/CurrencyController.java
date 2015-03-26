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
import com.gop.society.utils.AccountType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/api/currencies")
@Component("currencyController")
public class CurrencyController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @RequestMapping(value = "/{id}/add", method = RequestMethod.POST)
    @ResponseBody
    public void generateForCurrency(
            @PathParam("id") final String id,
            @RequestBody final Long totalToAdd)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomBadRequestException {
        final Currency currency = currencyService.get(id);
        final Organisation organisation = organisationService.get(currency.getOwnerId());
        final Account account = accountService.find(organisation.getId(), currency.getId(), AccountType.ORGANISATION);
        account.add(totalToAdd);
        accountService.update(account);
    }

    @RequestMapping(value = "/{id}/remove", method = RequestMethod.POST)
    @ResponseBody
    public void destroyForCurrency(
            @PathParam("id") final String id,
            @RequestBody final Long totalToRemove)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomBadRequestException {
        final Currency currency = currencyService.get(id);
        final Organisation organisation = organisationService.get(currency.getOwnerId());
        final Account account = accountService.find(organisation.getId(), currency.getId(), AccountType.ORGANISATION);
        account.take(totalToRemove);
        accountService.update(account);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(
            @PathParam("id") final String id)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomBadRequestException {
    }

}
