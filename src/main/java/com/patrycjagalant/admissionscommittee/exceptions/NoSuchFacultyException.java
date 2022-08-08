package com.patrycjagalant.admissionscommittee.exceptions;

public class NoSuchFacultyException extends Exception{
    public NoSuchFacultyException(){
        super();
    }
    public NoSuchFacultyException(String message) {
        super(message);
    }
}
