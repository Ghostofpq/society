package com.gop.society.utils;

/**
 * @author GhostOfPQ
 */
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String text;

    private UserRole(final String text) {
        this.text = text;
    }

    public static UserRole fromValue(final String text) {
        for (UserRole userRole : UserRole.values()) {
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
