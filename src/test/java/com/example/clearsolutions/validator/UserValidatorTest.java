package com.example.clearsolutions.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.exceptions.InvalidDateRangeException;
import com.example.clearsolutions.exceptions.UserUnderAgeException;

@DisplayName("UserValidator Test")
public class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    public void setUp() {
        userValidator = new UserValidator();
        ReflectionTestUtils.setField(userValidator, "minAge", 18);
    }

    @Test
    @DisplayName("Given user with valid age, when validate user, then no exception thrown")
    public void givenUserWithValidAge_whenValidateUser_thenNoExceptionThrown() {
        // Given
        UserDto user = new UserDto();
        user.setBirthDate(LocalDate.now().minusYears(20));

        // When
        // Then
        assertDoesNotThrow(() -> userValidator.validate(user,null));
    }

    @Test
    @DisplayName("Given user with exact min age, when validate user, then no exception thrown")
    public void givenUserWithExactMinAge_whenValidateUser_thenNoExceptionThrown() {
        // Given
        UserDto user = new UserDto();
        user.setBirthDate(LocalDate.now().minusYears(18));

        // When
        // Then
        assertDoesNotThrow(() -> userValidator.validate(user,null));
    }

    @Test
    @DisplayName("Given user under age, when validate user, then IllegalArgumentException thrown")
    public void givenUserUnderAge_whenValidateUser_thenIllegalArgumentExceptionThrown() {
        // Given
        UserDto user = new UserDto();
        user.setBirthDate(LocalDate.now().minusYears(17));

        // When
        // Then
        assertThrows(UserUnderAgeException.class, () -> userValidator.validate(user,null));
    }

    @Test
    @DisplayName("Given valid date range, when validate date range, then no exception thrown")
    public void givenValidDateRange_whenValidateDateRange_thenNoExceptionThrown() {
        // Given
        LocalDate from = LocalDate.now().minusDays(10);
        LocalDate to = LocalDate.now();

        // When
        // Then
        assertDoesNotThrow(() -> userValidator.validateDateRange(from, to));
    }

    @Test
    @DisplayName("Given from after to, when validate date range, then IllegalArgumentException thrown")
    public void givenFromAfterTo_whenValidateDateRange_thenIllegalArgumentExceptionThrown() {
        // Given
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().minusDays(10);

        // When
        // Then
        assertThrows(InvalidDateRangeException.class, () -> userValidator.validateDateRange(from, to));
    }

    @Test
    @DisplayName("Given from after to, when validate date range, then IllegalArgumentException thrown")
    public void givenFromEqualToTo_whenValidateDateRange_thenNoExceptionThrown() {
        // Given
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();

        // When
        // Then
        assertDoesNotThrow(() -> userValidator.validateDateRange(from, to));
    }
}