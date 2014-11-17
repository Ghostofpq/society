package com.gop.society.utils;

import lombok.*;

/**
 * @author GhostOfPQ
 */
@Data
public class UserCreationRequest {
    private String login;
    private String password;
    private String email;
}
