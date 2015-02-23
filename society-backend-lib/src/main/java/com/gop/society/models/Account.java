package com.gop.society.models;

import com.gop.society.utils.AccountType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author GhostOfPQ
 */
@Document
@Data
public class Account {
    @Id
    private String id;
    private AccountType accountType;

    @Indexed
    private String owner;
    @Indexed
    private String currency;

    private Long balance;
}
