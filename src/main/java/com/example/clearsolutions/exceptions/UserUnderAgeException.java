package com.example.clearsolutions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is under age")
public class UserUnderAgeException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserUnderAgeException(String message) {
        super(message);
    }
}
