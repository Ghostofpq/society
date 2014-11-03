package com.gop.society.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author GhostOfPQ
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Document
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String login;
    private String password;
    @Indexed(unique = true)
    private String email;
    private List<String> userRole;
}
