package com.example.timesheets2.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String objectName, Long id) {
        super("Could not find " + objectName + " " + id);
    }
}
