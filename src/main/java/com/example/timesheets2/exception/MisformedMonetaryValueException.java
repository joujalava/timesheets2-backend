package com.example.timesheets2.exception;

public class MisformedMonetaryValueException extends RuntimeException {
    public MisformedMonetaryValueException() {
        super("Monetary value is misformed");
    }
}
