package com.example.timesheets2.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("You are not logged in.");
    }
}
