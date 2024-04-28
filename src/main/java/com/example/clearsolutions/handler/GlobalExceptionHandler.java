package com.example.clearsolutions.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.clearsolutions.exceptions.InvalidDateRangeException;
import com.example.clearsolutions.exceptions.UserNotFoundException;
import com.example.clearsolutions.exceptions.UserUnderAgeException;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for handling all exceptions thrown across the application.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final long serialVersionUID = 1L;

    @ExceptionHandler(UserUnderAgeException.class)
    public ResponseEntity<?> handleUserUnderAgeException(UserUnderAgeException e) {
        log.error("User under age exception: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<?> handleInvalidDateRangeException(InvalidDateRangeException e) {
        log.error("Invalid date range exception: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleException(UserNotFoundException e) {
        log.error("User not found exception: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.error("Validation error: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
