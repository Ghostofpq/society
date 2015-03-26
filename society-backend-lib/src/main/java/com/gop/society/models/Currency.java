package com.gop.society.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author GhostOfPQ
 */
@Data
@Document
public class Currency {
    @Id
    private String id;
    private String ownerId;

    @Indexed(unique = true)
    private String name;
    private long total;
    private long numberOfUser;

    private long creationTs;
    private long updateTs;

    public Currency() {
        total = 0;
        numberOfUser = 0;
    }
}