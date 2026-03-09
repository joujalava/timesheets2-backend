package com.example.timesheets2.exception;

import java.util.List;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(List<String> uids) {
        super("User(s) " + String.join(", ", uids) + " not found");
    }
}
