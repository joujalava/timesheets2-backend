package com.example.timesheets2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MisformedMonetaryValueAdvice {

    @ExceptionHandler(MisformedMonetaryValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String misformedMonetaryValueAdvice(MisformedMonetaryValueException ex) {
        return ex.getMessage();
    }

}
