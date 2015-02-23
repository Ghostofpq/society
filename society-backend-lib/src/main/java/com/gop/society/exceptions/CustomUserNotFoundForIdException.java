package com.gop.society.exceptions;

/**
 * @author GhostOfPQ
 */
public class CustomUserNotFoundForIdException extends CustomNotFoundException {
    public CustomUserNotFoundForIdException(String id) {
        super("User not found for ID [" + id + "]");
    }
}
