package com.gop.society.controllers;

import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.models.Currency;
import com.gop.society.models.Organization;
import com.gop.society.models.User;
import com.gop.society.services.AccountService;
import com.gop.society.services.CurrencyService;
import com.gop.society.services.OrganizationService;
import com.gop.society.services.UserService;
import com.gop.society.utils.AccountCreationRequest;
import com.gop.society.utils.CurrencyCreationRequest;
import com.gop.society.utils.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashSet;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/api/currencies")
@Component("currencyController")
public class CurrencyController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AccountService accountService;

    @PostConstruct
    private void init() {
        log.info("CurrencyController started !");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Currency create(@RequestBody final CurrencyCreationRequest currencyCreationRequest) throws CustomBadRequestException, CustomNotFoundException {
        log.debug("In create with {}", currencyCreationRequest.toString());
        Organization organization = organizationService.get(currencyCreationRequest.getOrganizationInCharge());

        Currency currency = new Currency();

        currency.setTotal(0l);
        currency.setName(currencyCreationRequest.getName());
        currency.setOrganizationInCharge(currencyCreationRequest.getOrganizationInCharge());
        currency = currencyService.add(currency);

        organization.getManagedCurrencies().add(currency.getId());
        organizationService.update(organization);

        return currency;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Currency get(
            @PathVariable("id") final String id)
            throws CustomNotFoundException {
        return currencyService.get(id);
    }

    @RequestMapping(value = "/{id}/accounts", method = RequestMethod.POST)
    @ResponseBody
    public Account createAccount(
            @PathVariable("id") final String id,
            @RequestBody final AccountCreationRequest accountCreationRequest)
            throws CustomNotFoundException,
            CustomBadRequestException {
        Currency currency = currencyService.get(id);

        //Create account
        Account account = new Account();
        account.setBalance(0l);
        account.setCurrency(currency.getId());
        account.setAccountType(accountCreationRequest.getAccountType());
        account.setOwner(accountCreationRequest.getOwner());
        account = accountService.add(account);

        // Update owner account list
        switch (accountCreationRequest.getAccountType()) {
            case ORGANIZATION:
                Organization organization = organizationService.get(accountCreationRequest.getOwner());
                if (organization.getAccounts() == null) {
                    organization.setAccounts(new HashSet<String>());
                }
                organization.getAccounts().add(account.getId());
                organizationService.update(organization);
                break;
            case USER:
                User user = userService.get(accountCreationRequest.getOwner());
                if (user.getAccounts() == null) {
                    user.setAccounts(new HashSet<String>());
                }
                user.getAccounts().add(account.getId());
                userService.update(user);
                break;
        }

        return account;
    }

    @RequestMapping(value = "/{id}/orders", method = RequestMethod.POST)
    @ResponseBody
    public void createOrder(
            @PathVariable("id") final String id,
            @RequestBody final OrderRequest orderRequest)
            throws CustomNotFoundException, CustomBadRequestException {
        Currency currency = currencyService.get(id);

        switch (orderRequest.getOrderType()) {
            case GENERATE:
                currencyService.generate(currency, orderRequest.getDestination(), orderRequest.getBalance());
                break;
            case DESTROY:
                currencyService.destroy(currency, orderRequest.getDestination(), orderRequest.getBalance());
                break;
            case TRANSFER:
                currencyService.transfer(currency, orderRequest.getSource(), orderRequest.getDestination(), orderRequest.getBalance());
                break;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(
            @PathVariable("id") final String id)
            throws CustomNotFoundException {
        currencyService.delete(id);
    }
}
