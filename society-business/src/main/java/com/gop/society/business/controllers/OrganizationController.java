package com.gop.society.business.controllers;

import com.google.common.base.Strings;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.models.Currency;
import com.gop.society.models.Organization;
import com.gop.society.services.CurrencyService;
import com.gop.society.services.OrganizationService;
import com.gop.society.utils.OrganizationCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/organizations")
@Component("organizationController")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CurrencyService currencyService;

    @PostConstruct
    private void init() {
        log.info("OrganizationController started !");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Organization create(@RequestBody final OrganizationCreationRequest organizationToCreate) throws CustomBadRequestException, CustomNotFoundException {
        log.debug("In create with {}", organizationToCreate.toString());

        final Organization organization = new Organization();

        // Set Name
        organization.setName(organizationToCreate.getName());
        // Set Desc
        organization.setDescription(organizationToCreate.getDescription());

        organization.setAccountIds(new ArrayList<String>());

        organization.setAdmins(new HashSet<String>());
        organization.getAdmins().add(organizationToCreate.getCreatorId());

        if (!Strings.isNullOrEmpty(organizationToCreate.getCurrency())) {
            log.debug("Creating a currency named {}", organizationToCreate.getCurrency());
            Currency currency = new Currency(organizationToCreate.getCurrency());
            currency = currencyService.add(currency);
            Account account = currencyService.createAccount(currency.getId(), (organizationToCreate.getQuantity() != null ? organizationToCreate.getQuantity() : 0l));
            organization.getAccountIds().add(account.getId());
        }

        // Save
        return organizationService.add(organization);
    }

}
