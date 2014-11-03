package com.gop.society.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GhostOfPQ
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class UserCreationRequest {
    private String login;
    private String password;
    private String email;
}
