package com.example.clearsolutions.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.service.UserService;
import com.example.clearsolutions.validator.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for managing users.
 */
@Tag(name = "User Controller", description = "Operations pertaining to users")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    /**
     * Create a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto user) {
        userValidator.validate(user, null);
        UserDto createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Update specific fields of a user.
     *
     * @param id   the id of the user to update
     * @param user the user data to update
     * @return the updated user
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update specific fields of a user")
    public ResponseEntity<UserDto> updateUserFields(@PathVariable Long id, @Valid @RequestBody UserDto user) {
        UserDto updatedUser = userService.updateUserFields(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Update a user.
     *
     * @param id   the id of the user to update
     * @param user the user data to update
     * @return the updated user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a user")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto user) {
        UserDto updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Delete a user.
     *
     * @param id the id of the user to delete
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Search users by birthdate range.
     *
     * @param from the start of the birthdate range
     * @param to   the end of the birthdate range
     * @return the users found
     */
    @GetMapping("/search")
    @Operation(summary = "Search users by birth date range")
    public ResponseEntity<?> searchUsersByBirthDateRange(
            @RequestParam LocalDate from, @RequestParam LocalDate to) {
        userValidator.validateDateRange(from, to);

        List<UserDto> users = userService.searchUsersByBirthDateRange(from, to);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}

