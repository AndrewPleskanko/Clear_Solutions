package com.example.clearsolutions.exceptions;

public class UserUnderAgeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserUnderAgeException(String message) {
        super(message);
    }
}
