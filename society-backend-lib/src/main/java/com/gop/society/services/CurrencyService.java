package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.exceptions.CustomOrganizationNotFoundForIdException;
import com.gop.society.models.Currency;
import com.gop.society.repositories.CurrencyRepository;
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
@Component("currencyService")
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public Currency add(Currency currency) throws CustomBadRequestException {
        try {
            currency.setCreationTs(DateTime.now().getMillis());
            currency.setUpdateTs(DateTime.now().getMillis());
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
        throw new CustomOrganizationNotFoundForIdException(id);
    }

    public Currency update(Currency currency) throws CustomBadRequestException {
        try {
            currency.setUpdateTs(DateTime.now().getMillis());
            return currencyRepository.save(currency);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new currency.");
        }
    }

    public void delete(final String id) throws CustomNotFoundException {
        final Currency organisation = get(id);
        currencyRepository.delete(organisation);
    }

    public Pageable<Currency> getAll(final int pageNumber, final int size) {
        final Page<Currency> currencies = currencyRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<Currency>(
                currencies.getNumber(),
                currencies.getSize(),
                currencies.getTotalElements(),
                Lists.newArrayList(currencies.iterator()));
    }
}
