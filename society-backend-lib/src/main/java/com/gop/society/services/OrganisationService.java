package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.exceptions.CustomOrganizationNotFoundForIdException;
import com.gop.society.models.Organisation;
import com.gop.society.repositories.OrganisationRepository;
import com.gop.society.utils.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Component("organisationService")
public class OrganisationService {
    @Autowired
    private OrganisationRepository organisationRepository;

    public Organisation add(Organisation organisation) throws CustomBadRequestException {
        try {
            organisation.setCreationTs(DateTime.now().getMillis());
            organisation.setUpdateTs(DateTime.now().getMillis());
            return organisationRepository.save(organisation);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new Organisation.");
        }
    }

    public Organisation get(final String id) throws CustomNotFoundException {
        final Organisation organisation = organisationRepository.findOne(id);
        if (organisation != null) {
            return organisation;
        }
        throw new CustomOrganizationNotFoundForIdException(id);
    }

    public Organisation update(Organisation organisation) throws CustomBadRequestException {
        try {
            organisation.setUpdateTs(DateTime.now().getMillis());
            return organisationRepository.save(organisation);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new organisation.");
        }
    }

    public void delete(final String id) throws CustomNotFoundException {
        final Organisation organisation = get(id);
        organisationRepository.delete(organisation);
    }

    public Pageable<Organisation> getAll(final int pageNumber, final int size) {
        final Page<Organisation> organisations = organisationRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<Organisation>(
                organisations.getNumber(),
                organisations.getSize(),
                organisations.getTotalElements(),
                Lists.newArrayList(organisations.iterator()));
    }
}
