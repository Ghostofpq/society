package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.exceptions.CustomOrganizationNotFoundForIdException;
import com.gop.society.models.Organization;
import com.gop.society.repositories.OrganizationRepository;
import com.gop.society.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

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
    @Autowired
    private UserService userService;
    @Autowired
    private CurrencyService currencyService;

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

    public OrganizationVO getVO(final String id) throws CustomNotFoundException {
        final Organization organization = organizationRepository.findOne(id);

        final OrganizationVO organizationVO = new OrganizationVO();
        // ID
        organizationVO.setId(organization.getId());
        // Name
        organizationVO.setName(organization.getName());
        // Description
        organizationVO.setDescription(organization.getDescription());
        // Admins
        organizationVO.setAdmins(new HashSet<UserOrgaVO>());
        for (String userId : organization.getAdmins()) {
            organizationVO.getAdmins().add(userService.getOrgaVO(userId));
        }
        // Members
        organizationVO.setMembers(new HashSet<UserOrgaVO>());
        for (String userId : organization.getAdmins()) {
            organizationVO.getMembers().add(userService.getOrgaVO(userId));
        }
        // Accounts
        organizationVO.setAccounts(new HashSet<AccountVOUserView>());
        for (String accountId : organization.getAccounts()) {
            organizationVO.getAccounts().add(accountService.getVOUserView(accountId));
        }
        // Managed Currencies
        organizationVO.setManagedCurrencies(new HashSet<CurrencyVOUserView>());
        for (String currencyId : organization.getManagedCurrencies()) {
            organizationVO.getManagedCurrencies().add(currencyService.getVOUserView(currencyId));
        }
        return organizationVO;
    }


    public Organization update(final Organization organization) throws CustomBadRequestException {
        try {
            return organizationRepository.save(organization);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new organization.");
        }
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
