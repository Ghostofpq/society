package com.gop.society.repositories;

import com.gop.society.models.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author GhostOfPQ
 */
@Component("accountRepository")
public interface AccountRepository extends PagingAndSortingRepository<Account, String> {
    public Account findFirstByOwnerAndCurrency(String owner, String currency);
}
