package com.gop.society.repositories;

import com.gop.society.models.Account;
import com.gop.society.utils.AccountType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author GhostOfPQ
 */
@Component("accountRepository")
public interface AccountRepository extends PagingAndSortingRepository<Account, String> {
    public Account findByOwnerIdAndCurrencyIdAndAccountType(final String ownerId, final String currencyId, final AccountType accountType);
}
