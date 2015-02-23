package com.gop.society.utils;

/**
 * @author GhostOfPQ
 */
public enum OrderType {
    TRANSFER("Order Transfer"),
    GENERATE("Order Generate"),
    DESTROY("Order Destroy");

    private final String text;

    private OrderType(final String text) {
        this.text = text;
    }

    public static OrderType fromValue(final String text) {
        for (OrderType userRole : OrderType.values()) {
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
