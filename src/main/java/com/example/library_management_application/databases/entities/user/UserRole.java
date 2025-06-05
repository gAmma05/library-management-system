package com.example.library_management_application.databases.entities.user;

public enum UserRole {
    ADMIN(0),
    MEMBER(1);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserRole fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (UserRole role : UserRole.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role value: " + value);

    }
}
