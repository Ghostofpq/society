package com.gop.society.utils;

import lombok.Data;

/**
 * @author GhostOfPQ
 */
@Data
public class OrderRequest {
    private String source;
    private String destination;
    private Long balance;
}
