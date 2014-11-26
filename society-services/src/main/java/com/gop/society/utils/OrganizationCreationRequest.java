package com.gop.society.utils;

import lombok.Data;

/**
 * @author GhostOfPQ
 */
@Data
public class OrganizationCreationRequest {
    private String creatorId;
    private String name;
    private String description;
    private String currency;
    private Long quantity;
}
