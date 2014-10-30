package com.gop.society.repositories;

import com.gop.society.models.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author GhostOfPQ
 */
@Component
@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, String> {
}
