package com.example.clearsolutions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid date range")
public class InvalidDateRangeException extends UserException {
    private static final long serialVersionUID = 1L;

    public InvalidDateRangeException(String message) {
        super(message);
    }
}