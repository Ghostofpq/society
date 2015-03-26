package com.gop.society.models;

import com.gop.society.exceptions.CustomAccountHasNotEnoughFoundForOrderException;
import com.gop.society.utils.AccountType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author GhostOfPQ
 */
@Data
@Document
public class Account {
    @Id
    private String id;
    private AccountType accountType;
    private String ownerId;
    private String currencyId;
    private Long balance;
    private long creationTs;
    private long updateTs;

    public void add(final Long balance) {
        this.balance += balance;
    }

    public void take(final Long balance) throws CustomAccountHasNotEnoughFoundForOrderException {
        if (this.balance < balance) {
            throw new CustomAccountHasNotEnoughFoundForOrderException(id);
        }
        this.balance -= balance;
    }
}
