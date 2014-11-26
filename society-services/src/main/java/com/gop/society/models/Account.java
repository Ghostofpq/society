package com.gop.society.models;

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

    private String currencyId;
    private Long balance;

    public Account(String currencyId) {
        this.currencyId = currencyId;
        this.balance = 0l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (currencyId != null ? !currencyId.equals(account.currencyId) : account.currencyId != null) return false;
        if (id != null ? !id.equals(account.id) : account.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (currencyId != null ? currencyId.hashCode() : 0);
        return result;
    }
}
