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
public class OrganisationCreationRequest {
    private String name;
    private String description;
}
