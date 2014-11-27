package com.gop.society.repositories;

import com.gop.society.models.Currency;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author GhostOfPQ
 */
@Component("currencyRepository")
@Repository
public interface CurrencyRepository extends PagingAndSortingRepository<Currency, String> {
    public Currency findByName(final String name);
}