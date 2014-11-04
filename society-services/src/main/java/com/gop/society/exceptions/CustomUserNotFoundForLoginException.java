package com.gop.society.exceptions;

/**
 * @author GhostOfPQ
 */
public class CustomUserNotFoundForLoginException extends CustomNotFoundException {
    public CustomUserNotFoundForLoginException(String login) {
        super("User not found for Login [" + login + "]");
    }
}
