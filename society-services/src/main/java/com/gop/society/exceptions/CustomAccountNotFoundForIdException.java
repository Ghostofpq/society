package com.gop.society.exceptions;

/**
 * @author GhostOfPQ
 */
public class CustomAccountNotFoundForIdException extends CustomNotFoundException {
    public CustomAccountNotFoundForIdException(String id) {
        super("Account not found for ID [" + id + "]");
    }
}
