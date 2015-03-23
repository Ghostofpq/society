package com.gop.society.utils;

import lombok.Data;

/**
 * @author GhostOfPQ
 */
@Data
public class OrganisationCreationRequest {
    private String creator;
    private String name;
    private String description;
}
