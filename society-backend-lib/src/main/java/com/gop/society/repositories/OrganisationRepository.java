package com.gop.society.repositories;

import com.gop.society.models.Organisation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author GhostOfPQ
 */
@Component("organisationRepository")
public interface OrganisationRepository extends PagingAndSortingRepository<Organisation, String> {
    public Organisation findByName(final String login);
}
