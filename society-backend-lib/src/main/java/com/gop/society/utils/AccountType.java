package com.gop.society.utils;

/**
 * @author GhostOfPQ
 */
public enum AccountType {
    USER("User Account"),
    ORGANISATION("Organisation Account");

    private final String text;

    private AccountType(final String text) {
        this.text = text;
    }

    public static AccountType fromValue(final String text) {
        for (AccountType userRole : AccountType.values()) {
            if (userRole.toString().equals(text)) {
                return userRole;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}
