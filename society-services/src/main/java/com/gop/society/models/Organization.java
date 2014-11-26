package com.gop.society.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

/**
 * @author GhostOfPQ
 */
@Data
@Document
public class Organization {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String description;

    private Set<String> admins;
    private Set<String> members;

    private List<String> accountIds;
}
