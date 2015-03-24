package com.gop.society.utils;

import lombok.Data;

/**
 * @author GhostOfPQ
 */
@Data
public class CurrencyCreationRequest {
    private String name;
    private long initialAmount;
}
