package com.gop.society.repositories;

import com.gop.society.models.Currency;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author GhostOfPQ
 */
@Component("currencyRepository")
public interface CurrencyRepository extends PagingAndSortingRepository<Currency, String> {
    public Currency findByName(final String login);
}
