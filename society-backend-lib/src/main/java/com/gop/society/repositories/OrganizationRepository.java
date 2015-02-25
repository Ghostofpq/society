package com.gop.society.repositories;

import com.gop.society.models.Organization;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author GhostOfPQ
 */
@Component("organizationRepository")
public interface OrganizationRepository extends PagingAndSortingRepository<Organization, String> {
    public Organization findByName(final String name);
}
