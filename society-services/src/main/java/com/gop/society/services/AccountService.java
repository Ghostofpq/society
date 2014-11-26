package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomAccountNotFoundForIdException;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.repositories.AccountRepository;
import com.gop.society.utils.Pageable;
import lombok.extern.slf4j.Slf4j;
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
        throw new CustomAccountNotFoundForIdException(id);
    }

    public Account update(final Account account) throws CustomNotFoundException {
        return accountRepository.save(account);
    }

    public void transfert(final String idGiver, final String idReceiver, final Long quantity) throws CustomBadRequestException {
        final Account accountGiver = accountRepository.findOne(idGiver);
        final Account accountReceiver = accountRepository.findOne(idReceiver);
        if (accountGiver.getBalance() >= quantity) {
            accountGiver.setBalance(accountGiver.getBalance() - quantity);
            accountReceiver.setBalance(accountReceiver.getBalance() + quantity);
            accountRepository.save(accountGiver);
            accountRepository.save(accountReceiver);
        } else {
            throw new CustomBadRequestException("Account [{}] cannot give [{}] to [{}], insufficient funds!");
        }
    }

    public void delete(final String id) throws CustomNotFoundException {
        final Account account = get(id);
        accountRepository.delete(account);
    }

    public Pageable<Account> getAll(final int pageNumber, final int size) {
        final Page<Account> accounts = accountRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<Account>(
                accounts.getNumber(),
                accounts.getSize(),
                accounts.getTotalElements(),
                Lists.newArrayList(accounts.iterator()));
    }

}
