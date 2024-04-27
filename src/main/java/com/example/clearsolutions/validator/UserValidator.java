package com.example.clearsolutions.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.clearsolutions.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

/**
 * Validator class for User related validations.
 */
@Slf4j
@Component
public class UserValidator {

    @Value("${user.min.age}")
    private int minAge;

    /**
     * Validates the user's age.
     *
     * @param userDto the user data transfer object
     * @throws IllegalArgumentException if the user's age is less than the minimum age
     */
    public void validateUser(UserDto userDto) {
        if (userDto.getBirthDate().isAfter(LocalDate.now().minusYears(minAge))) {
            log.error("User must be at least {} years old", minAge);
            throw new IllegalArgumentException("User must be at least " + minAge + " years old");
        }
        log.info("User validation passed for {}", userDto);
    }

    /**
     * Validates the date range.
     *
     * @param from the start of the date range
     * @param to the end of the date range
     * @throws IllegalArgumentException if the 'from' date is after the 'to' date
     */
    public void validateDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            log.error("'From' date must be less than 'To' date");
            throw new IllegalArgumentException("'From' date must be less than 'To' date");
        }
        log.info("Date range validation passed for from: {}, to: {}", from, to);
    }
}