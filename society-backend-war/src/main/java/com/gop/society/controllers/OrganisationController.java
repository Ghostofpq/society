package com.gop.society.controllers;

import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Organisation;
import com.gop.society.security.CustomAuthenticationProvider;
import com.gop.society.services.OrganisationService;
import com.gop.society.services.UserService;
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
    private CustomAuthenticationProvider customAuthenticationProvider;

    @PostConstruct
    private void init() {
        log.info("OrganisationController started !");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Organisation create(@RequestBody final OrganisationCreationRequest organisationCreationRequest) throws CustomBadRequestException {
        log.debug("create({})", organisationCreationRequest.toString());
        final Organisation organisation = new Organisation();
        organisation.setName(organisationCreationRequest.getName());
        organisation.setDescription(organisationCreationRequest.getDescription());
        organisation.addManager(organisationCreationRequest.getCreator());
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
        Organisation organisation = organisationService.get(id);
        organisation.addMember(customAuthenticationProvider.getAuthenticatedUserId());
        return organisationService.update(organisation);
    }
}
