package com.gop.society.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GhostOfPQ
 */
@Data
@Document
public class Currency {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;

    private Long total;
    private List<String> accountIds;

    public Currency(final String name) {
        this.name = name;
        this.total = 0l;
        this.accountIds = new ArrayList<>();
    }
}
