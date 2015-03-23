package com.gop.society.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * @author GhostOfPQ
 */
@Data
@Document
public class Organisation {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private String description;

    private Set<String> managers;
    private Set<String> members;

    private long creationTs;
    private long updateTs;

    public Organisation() {
        managers = new HashSet<>();
        members = new HashSet<>();
    }

    public void addManager(String userId) {
        managers.add(userId);
        members.add(userId);
    }

    public void addMember(String userId) {
        members.add(userId);
    }
}
