package com.patrycjagalant.admissionscommittee.entity;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("pending"),
    REJECTED("rejected"),
    BUDGET("budget"),
    CONTRACT("contract");

    private final String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }
}
