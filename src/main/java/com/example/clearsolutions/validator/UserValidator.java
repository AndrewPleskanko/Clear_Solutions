package com.example.clearsolutions.validator;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.exceptions.InvalidDateRangeException;
import com.example.clearsolutions.exceptions.UserUnderAgeException;
import lombok.extern.slf4j.Slf4j;

/**
 * Validator class for User related validations.
 */
@Slf4j
@Component
public class UserValidator implements Validator {

    @Value("${user.min.age}")
    private int minAge;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;

        int age = Period.between(userDto.getBirthDate(), LocalDate.now()).getYears();
        if (age < minAge) {
            log.error("User must be at least {} years old", minAge);
            throw new UserUnderAgeException("User must be at least " + minAge + " years old");
        }
        log.info("Validation for UserDto completed successfully");
    }

    public void validateDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            String errorMessage = String.format("'From' date %s must be less than 'To' date %s", from, to);
            log.error(errorMessage);
            throw new InvalidDateRangeException(errorMessage);
        }
        log.info("Date range validation passed for from: {}, to: {}", from, to);
    }
}