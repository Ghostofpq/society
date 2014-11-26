package com.gop.society.exceptions;

/**
 * @author GhostOfPQ
 */
public class CustomCurrencyNotFoundForIdException extends CustomNotFoundException {
    public CustomCurrencyNotFoundForIdException(String id) {
        super("Organization not found for ID [" + id + "]");
    }
}
