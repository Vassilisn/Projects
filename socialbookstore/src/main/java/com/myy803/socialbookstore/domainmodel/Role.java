package com.myy803.socialbookstore.domainmodel;

public enum Role {
	USER("User"),
    ADMIN("Admin");

    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
