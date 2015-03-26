package com.gop.society.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GhostOfPQ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyCreationRequest {
    private String name;
    private long initialAmount;
}
