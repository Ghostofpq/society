package com.gop.society.repositories;

import com.gop.society.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author GhostOfPQ
 */
@Component("userRepository")
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    public User findByLogin(final String login);
}
