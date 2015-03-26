package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.repositories.AccountRepository;
import com.gop.society.utils.AccountType;
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
@Component("accountService")
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account add(Account account) throws CustomBadRequestException {
        try {
            account.setCreationTs(DateTime.now().getMillis());
            account.setUpdateTs(DateTime.now().getMillis());
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new account.");
        }
    }

    public Account get(final String id) throws CustomNotFoundException {
        final Account account = accountRepository.findOne(id);
        if (account != null) {
            return account;
        }
        throw new CustomNotFoundException("Account not found");
    }

    public Account find(final String ownerId, final String currencyId, final AccountType accountType) throws CustomNotFoundException {
        final Account account = accountRepository.findByOwnerIdAndCurrencyIdAndAccountType(ownerId, currencyId, accountType);
        if (account != null) {
            return account;
        }
        throw new CustomNotFoundException("Account not found");
    }

    public Account update(Account account) throws CustomBadRequestException {
        try {
            account.setUpdateTs(DateTime.now().getMillis());
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new account.");
        }
    }

    public void delete(final String id) throws CustomNotFoundException {
        final Account organisation = get(id);
        accountRepository.delete(organisation);
    }

    public Pageable<Account> getAll(final int pageNumber, final int size) {
        final Page<Account> currencies = accountRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<Account>(
                currencies.getNumber(),
                currencies.getSize(),
                currencies.getTotalElements(),
                Lists.newArrayList(currencies.iterator()));
    }
}
