package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomCurrencyNotFoundForIdException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.models.Currency;
import com.gop.society.repositories.CurrencyRepository;
import com.gop.society.utils.CurrencyVOUserView;
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

    public CurrencyVOUserView getVOUserView(final String id) throws CustomNotFoundException {
        final Currency currency = currencyRepository.findOne(id);
        if (currency != null) {
            final CurrencyVOUserView currencyVOUserView = new CurrencyVOUserView();
            currencyVOUserView.setId(currency.getId());
            currencyVOUserView.setName(currency.getName());
            return currencyVOUserView;
        }
        throw new CustomCurrencyNotFoundForIdException(id);
    }

    public Currency update(final Currency currency) throws CustomNotFoundException {
        return currencyRepository.save(currency);
    }

    public void transfer(Currency currency, final String idGiver, final String idReceiver, final Long quantity) throws CustomBadRequestException, CustomNotFoundException {
        Account accountReceiver = accountService.findByOwnerAndCurrency(idReceiver,currency.getId());
        Account accountGiver = accountService.findByOwnerAndCurrency(idGiver,currency.getId());

        if (accountGiver.getBalance() >= quantity) {
            accountGiver.setBalance(accountGiver.getBalance() - quantity);
            accountReceiver.setBalance(accountReceiver.getBalance() + quantity);
            accountService.update(accountGiver);
            accountService.update(accountReceiver);
        } else {
            throw new CustomBadRequestException("[" + idGiver + "] cannot give [" + quantity + "] to [" + idReceiver + "], insufficient funds!");
        }
    }

    public void generate(Currency currency, final String idReceiver, final Long balance) throws CustomBadRequestException, CustomNotFoundException {
        Account accountReceiver = accountService.findByOwnerAndCurrency(idReceiver,currency.getId());
        accountReceiver.setBalance(accountReceiver.getBalance() + balance);
        accountService.update(accountReceiver);

        currency.setTotal(currency.getTotal() + balance);
        update(currency);
    }

    public void destroy(Currency currency, final String idGiver, final Long balance) throws CustomBadRequestException, CustomNotFoundException {
        Account accountGiver = accountService.findByOwnerAndCurrency(idGiver,currency.getId());
        if (accountGiver.getBalance() >= balance) {
            accountGiver.setBalance(accountGiver.getBalance() - balance);
            accountService.update(accountGiver);
        } else {
            throw new CustomBadRequestException("[" + idGiver + "] cannot destroy [" + balance + "], insufficient funds!");
        }

        currency.setTotal(currency.getTotal() - balance);
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
