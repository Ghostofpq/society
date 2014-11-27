package com.gop.society.utils;

import lombok.Data;

/**
 * @author GhostOfPQ
 */
@Data
public class AccountCreationRequest {
    private AccountType accountType;
    private String owner;
}
