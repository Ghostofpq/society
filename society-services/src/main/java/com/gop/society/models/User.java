package com.gop.society.models;

import com.gop.society.utils.UserRole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author GhostOfPQ
 */
@Data
@Document
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String login;

    private String password;

    @Indexed(unique = true)
    private String email;
    private String salt;

    private List<UserRole> userRole;
}
