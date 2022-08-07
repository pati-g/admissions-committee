package com.patrycjagalant.admissionscommittee.entity;

public enum Role {
    USER("user"), ADMIN("admin");

    private final String label;

    private Role(String label){
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
