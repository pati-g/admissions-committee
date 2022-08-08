package com.patrycjagalant.admissionscommittee.exceptions;

public class NoSuchApplicantException extends Exception{
    public NoSuchApplicantException(){
        super();
    }
    public NoSuchApplicantException(String message) {
        super(message);
    }
}
