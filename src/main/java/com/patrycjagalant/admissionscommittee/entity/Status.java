package com.patrycjagalant.admissionscommittee.entity;

public enum Status {
    P("pending"), R("rejected"), B("enrolled for budget"), C("enrolled for contract");

    private final String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }
}
