package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomCurrencyNotFoundForIdException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.models.Currency;
import com.gop.society.repositories.CurrencyRepository;
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
@Component("currencyService")
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private AccountService accountService;

    public Currency add(Currency currency) throws CustomBadRequestException {
        try {
            return currencyRepository.save(currency);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new currency.");
        }
    }

    public Currency get(final String id) throws CustomNotFoundException {
        final Currency currency = currencyRepository.findOne(id);
        if (currency != null) {
            return currency;
        }
        throw new CustomCurrencyNotFoundForIdException(id);
    }

    public Currency update(final Currency currency) throws CustomNotFoundException {
        return currencyRepository.save(currency);
    }

    public Account createAccount(final String idCurrency, final Long quantity) throws CustomBadRequestException, CustomNotFoundException {
        Currency currency = get(idCurrency);

        Account account = new Account(idCurrency);
        account = accountService.add(account);

        currency.getAccountIds().add(account.getId());
        update(currency);
        generateMoney(idCurrency, account.getId(), quantity);

        return account;
    }


    public void transfert(final String idCurrency, final String idGiver, final String idReceiver, final Long quantity) throws CustomBadRequestException, CustomNotFoundException {
        Currency currency = get(idCurrency);

        if (!currency.getAccountIds().contains(idGiver)) {
            throw new CustomBadRequestException("[" + idGiver + "] is not an account of [" + currency.getName() + "]!");
        }
        if (!currency.getAccountIds().contains(idReceiver)) {
            throw new CustomBadRequestException("[" + idReceiver + "] is not an account of [" + currency.getName() + "]!");
        }

        Account accountGiver = accountService.get(idGiver);
        Account accountReceiver = accountService.get(idReceiver);

        if (accountGiver.getBalance() >= quantity) {
            accountGiver.setBalance(accountGiver.getBalance() - quantity);
            accountReceiver.setBalance(accountReceiver.getBalance() + quantity);
            accountService.update(accountGiver);
            accountService.update(accountReceiver);
        } else {
            throw new CustomBadRequestException("[" + idGiver + "] cannot give [" + quantity + "] to [" + idReceiver + "], insufficient funds!");
        }
    }

    public void generateMoney(final String idCurrency, final String idReceiver, final Long quantity) throws CustomBadRequestException, CustomNotFoundException {
        Currency currency = get(idCurrency);

        if (!currency.getAccountIds().contains(idReceiver)) {
            throw new CustomBadRequestException("[" + idReceiver + "] is not an account of [" + currency.getName() + "]!");
        }

        Account accountReceiver = accountService.get(idReceiver);
        accountReceiver.setBalance(accountReceiver.getBalance() + quantity);
        accountService.update(accountReceiver);

        currency.setTotal(currency.getTotal() + quantity);
        update(currency);
    }

    public void destroyMoney(final String idCurrency, final String idGiver, final Long quantity) throws CustomBadRequestException, CustomNotFoundException {
        Currency currency = get(idCurrency);

        if (!currency.getAccountIds().contains(idGiver)) {
            throw new CustomBadRequestException("[" + idGiver + "] is not an account of [" + currency.getName() + "]!");
        }

        Account accountGiver = accountService.get(idGiver);
        if (accountGiver.getBalance() >= quantity) {
            accountGiver.setBalance(accountGiver.getBalance() - quantity);
            accountService.update(accountGiver);
        } else {
            throw new CustomBadRequestException("[" + idGiver + "] cannot destroy [" + quantity + "], insufficient funds!");
        }

        currency.setTotal(currency.getTotal() - quantity);
        update(currency);
    }


    public void delete(final String id) throws CustomNotFoundException {
        final Currency currency = get(id);
        currencyRepository.delete(currency);
    }

    public Pageable<Currency> getAll(final int pageNumber, final int size) {
        final Page<Currency> currencys = currencyRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<Currency>(
                currencys.getNumber(),
                currencys.getSize(),
                currencys.getTotalElements(),
                Lists.newArrayList(currencys.iterator()));
    }

}
