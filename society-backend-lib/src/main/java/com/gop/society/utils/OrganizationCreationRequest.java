package com.gop.society.utils;

import lombok.Data;

/**
 * @author GhostOfPQ
 */
@Data
public class OrganizationCreationRequest {
    private String creator;
    private String name;
    private String description;
}
