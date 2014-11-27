package com.gop.society.business.controllers;

import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Organization;
import com.gop.society.services.CurrencyService;
import com.gop.society.services.OrganizationService;
import com.gop.society.services.QueryOrganizationService;
import com.gop.society.services.UserService;
import com.gop.society.utils.OrganizationCreationRequest;
import com.gop.society.utils.OrganizationVO;
import com.gop.society.utils.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/organizations")
@Component("organizationController")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private QueryOrganizationService queryOrganizationService;

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
        // Init lists
        organization.setAdmins(new HashSet<String>());
        organization.setMembers(new HashSet<String>());
        organization.setAccounts(new HashSet<String>());
        organization.setManagedCurrencies(new HashSet<String>());
        // Add creator as admin and member
        organization.getAdmins().add(organizationToCreate.getCreator());
        organization.getMembers().add(organizationToCreate.getCreator());
        // Save
        return organizationService.add(organization);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Pageable<Organization> getByParameters(
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "user", required = false) final String user,
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "10") final Integer size)
            throws CustomBadRequestException, CustomNotFoundException {
        log.debug("In getByParameters");
        return queryOrganizationService.getByParameters(name, user, pageNumber, size);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public OrganizationVO get(
            @PathVariable("id") final String id)
            throws CustomNotFoundException {
        return organizationService.getVO(id);
    }

    @RequestMapping(value = "/{id}/name", method = RequestMethod.PUT)
    @ResponseBody
    public Organization updateName(
            @PathVariable("id") final String id,
            @RequestBody final String name)
            throws CustomNotFoundException,
            CustomBadRequestException {
        Organization organization = organizationService.get(id);
        organization.setName(name);
        return organizationService.update(organization);
    }

    @RequestMapping(value = "/{id}/description", method = RequestMethod.PUT)
    @ResponseBody
    public Organization updateDescription(
            @PathVariable("id") final String id,
            @RequestBody final String description)
            throws CustomNotFoundException,
            CustomBadRequestException {
        Organization organization = organizationService.get(id);
        organization.setDescription(description);
        return organizationService.update(organization);
    }

    @RequestMapping(value = "/{id}/members", method = RequestMethod.POST)
    @ResponseBody
    public Organization addMember(
            @PathVariable("id") final String id,
            @RequestBody final String userId)
            throws CustomNotFoundException,
            CustomBadRequestException {
        Organization organization = organizationService.get(id);
        userService.get(userId);
        organization.getMembers().add(userId);
        return organizationService.update(organization);
    }

    @RequestMapping(value = "/{id}/members/{memberId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Organization removeMember(
            @PathVariable("id") final String id,
            @PathVariable("memberId") final String memberId)
            throws CustomNotFoundException,
            CustomBadRequestException {
        Organization organization = organizationService.get(id);
        userService.get(memberId);
        organization.getMembers().remove(memberId);
        return organizationService.update(organization);
    }

    @RequestMapping(value = "/{id}/admins", method = RequestMethod.POST)
    @ResponseBody
    public Organization addAdmin(
            @PathVariable("id") final String id,
            @RequestBody final String userId)
            throws CustomNotFoundException,
            CustomBadRequestException {
        Organization organization = organizationService.get(id);
        userService.get(userId);
        organization.getAdmins().add(userId);
        return organizationService.update(organization);
    }

    @RequestMapping(value = "/{id}/admins/{adminId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Organization removeAdmin(
            @PathVariable("id") final String id,
            @PathVariable("adminId") final String adminId)
            throws CustomNotFoundException,
            CustomBadRequestException {
        Organization organization = organizationService.get(id);
        userService.get(adminId);
        organization.getAdmins().remove(adminId);
        return organizationService.update(organization);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(
            @PathVariable("id") final String id)
            throws CustomNotFoundException {
        organizationService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Pageable<Organization> getByParameters(
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "10") final Integer size) {
        return organizationService.getAll(pageNumber, size);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseBody
    private String handleNotFoundException(CustomNotFoundException e) {
        log.error(HttpStatus.NOT_FOUND + ":" + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CustomNotAuthorizedException.class)
    @ResponseBody
    private String handleNotAuthorizedException(CustomNotAuthorizedException e) {
        log.error(HttpStatus.FORBIDDEN + ":" + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomBadRequestException.class)
    @ResponseBody
    private String handleBadRequestException(CustomBadRequestException e) {
        log.error(HttpStatus.BAD_REQUEST + ":" + e.getMessage());
        return e.getMessage();
    }
    // if (!Strings.isNullOrEmpty(organizationToCreate.getCurrency())) {
    //     log.debug("Creating a currency named {}", organizationToCreate.getCurrency());
    //     Currency currency = new Currency(organizationToCreate.getCurrency());
    //     currency = currencyService.add(currency);
    //     Account account = currencyService.createAccount(currency.getId(), (organizationToCreate.getQuantity() != null ? organizationToCreate.getQuantity() : 0l));
    //     organization.getAccounts().add(account.getId());
    // }
}
