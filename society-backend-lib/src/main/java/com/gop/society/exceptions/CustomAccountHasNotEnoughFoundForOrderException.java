package com.gop.society.exceptions;

/**
 * @author GhostOfPQ
 */
public class CustomAccountHasNotEnoughFoundForOrderException extends CustomNotFoundException {
    public CustomAccountHasNotEnoughFoundForOrderException(String id) {
        super("Account [" + id + "] does not have enough found for order.");
    }
}
