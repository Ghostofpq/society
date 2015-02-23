package com.gop.society.exceptions;

/**
 * @author GhostOfPQ
 */
public class CustomOrganizationNotFoundForIdException extends CustomNotFoundException {
    public CustomOrganizationNotFoundForIdException(String id) {
        super("Organization not found for ID [" + id + "]");
    }
}
