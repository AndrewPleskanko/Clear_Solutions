package com.example.clearsolutions.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.clearsolutions.exceptions.UserException;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for handling all exceptions thrown across the application.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final long serialVersionUID = 1L;

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException e) {
        log.error("Handle user exception: {}", e.getMessage());
        HttpStatus httpStatus = e.getClass().getAnnotation(ResponseStatus.class).code();
        return new ResponseEntity<>(e.getMessage(), httpStatus);
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
