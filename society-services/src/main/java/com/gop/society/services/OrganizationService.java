package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.exceptions.CustomOrganizationNotFoundForIdException;
import com.gop.society.models.Account;
import com.gop.society.models.Organization;
import com.gop.society.repositories.OrganizationRepository;
import com.gop.society.utils.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Component("organizationService")
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private AccountService accountService;

    @PostConstruct
    private void init() throws NoSuchAlgorithmException {
    }

    public Organization add(Organization organization) throws CustomBadRequestException {
        try {
            return organizationRepository.save(organization);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new organization.");
        }
    }

    public Organization get(final String id) throws CustomNotFoundException {
        final Organization organization = organizationRepository.findOne(id);
        if (organization != null) {
            return organization;
        }
        throw new CustomOrganizationNotFoundForIdException(id);
    }

    public Organization update(final Organization organization) throws CustomBadRequestException {
        try {
            return organizationRepository.save(organization);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new organization.");
        }
    }

    public void generateMoney(final String accountId, final Long quantity) throws CustomBadRequestException, CustomNotFoundException {
        Account organizationAccount = accountService.get(accountId);
        organizationAccount.setBalance(organizationAccount.getBalance() + quantity);
        accountService.update(organizationAccount);
    }


    public void delete(final String id) throws CustomNotFoundException {
        final Organization organization = get(id);
        organizationRepository.delete(organization);
    }

    public Pageable<Organization> getAll(final int pageNumber, final int size) {
        final Page<Organization> organizations = organizationRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<Organization>(
                organizations.getNumber(),
                organizations.getSize(),
                organizations.getTotalElements(),
                Lists.newArrayList(organizations.iterator()));
    }
}
