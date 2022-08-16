package com.patrycjagalant.admissionscommittee.entity;

import lombok.Getter;

@Getter
public enum Status {
    P("pending"), R("rejected"), B("budget"), C("contract");

    private final String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }
}
