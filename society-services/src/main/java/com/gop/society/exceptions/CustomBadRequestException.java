package com.gop.society.exceptions;

/**
 * @author GhostOfPQ
 */
public class CustomBadRequestException extends Exception {
    public CustomBadRequestException(String message) {
        super(message);
    }
}
