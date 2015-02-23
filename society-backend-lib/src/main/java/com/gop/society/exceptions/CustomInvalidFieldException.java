package com.gop.society.exceptions;

/**
 * @author GhostOfPQ
 */
public class CustomInvalidFieldException extends CustomBadRequestException {
    public CustomInvalidFieldException(String fieldName) {
        super("Field[" + fieldName + "] is invalid.");
    }
}
