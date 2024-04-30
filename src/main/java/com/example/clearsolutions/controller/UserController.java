package com.example.clearsolutions.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.example.clearsolutions.validator.UserDtoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing users.
 */
@Slf4j
@Tag(name = "User Controller", description = "Operations pertaining to users")
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private static final String RESPONSE_CODE_200 = "200";
    private static final String RESPONSE_CODE_201 = "201";
    private static final String RESPONSE_CODE_204 = "204";
    private static final String RESPONSE_CODE_400 = "400";
    private static final String RESPONSE_CODE_404 = "404";
    private static final String RESPONSE_CODE_500 = "500";
    private static final String USER_CREATED_SUCCESSFULLY = "User created successfully";
    private static final String INVALID_USER_INPUT = "Invalid user input";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";
    private static final String USER_UPDATED_SUCCESSFULLY = "User updated successfully";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private static final String USERS_FOUND_SUCCESSFULLY = "Users found successfully";
    private static final String INVALID_DATE_RANGE_INPUT = "Invalid date range input";

    private final UserService userService;
    private final UserDtoValidator userDtoValidator;

    /**
     * Create a new user.
     *
     * @param userDto the user to create
     * @return the created user
     */
    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_CODE_201, description = USER_CREATED_SUCCESSFULLY,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_404, description = INVALID_USER_INPUT,
                    content = @Content),
            @ApiResponse(responseCode = RESPONSE_CODE_500, description = INTERNAL_SERVER_ERROR,
                    content = @Content)})
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Received request to create user: {}", userDto);
        userDtoValidator.validateUser(userDto);
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Update specific fields of a user.
     *
     * @param id      the id of the user to update
     * @param userDto the user data to update
     * @return the updated user
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update specific fields of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_CODE_200, description = USER_UPDATED_SUCCESSFULLY,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_400, description = INVALID_USER_INPUT,
                    content = @Content),
            @ApiResponse(responseCode = RESPONSE_CODE_404, description = USER_NOT_FOUND,
                    content = @Content),
            @ApiResponse(responseCode = RESPONSE_CODE_500, description = INTERNAL_SERVER_ERROR,
                    content = @Content)})
    public ResponseEntity<UserDto> updateUserFields(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("Received request to update user fields for id: {}, with data: {}", id, userDto);
        userDtoValidator.validateUser(userDto);
        UserDto updatedUser = userService.updateUserFields(id, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Update a user.
     *
     * @param id      the id of the user to update
     * @param userDto the user data to update
     * @return the updated user
     */

    @PutMapping("/{id}")
    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_CODE_200, description = USER_UPDATED_SUCCESSFULLY,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_400, description = INVALID_USER_INPUT,
                    content = @Content),
            @ApiResponse(responseCode = RESPONSE_CODE_404, description = USER_NOT_FOUND,
                    content = @Content),
            @ApiResponse(responseCode = RESPONSE_CODE_500, description = INTERNAL_SERVER_ERROR,
                    content = @Content)})
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("Received request to update user with id: {}, with data: {}", id, userDto);
        userDtoValidator.validateUser(userDto);
        UserDto updatedUser = userService.updateUser(id, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Delete a user.
     *
     * @param id the id of the user to delete
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_CODE_204, description = USER_DELETED_SUCCESSFULLY,
                    content = @Content),
            @ApiResponse(responseCode = RESPONSE_CODE_404, description = USER_NOT_FOUND,
                    content = @Content),
            @ApiResponse(responseCode = RESPONSE_CODE_500, description = INTERNAL_SERVER_ERROR,
                    content = @Content)})
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with id: {}", id);
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_CODE_200, description = USERS_FOUND_SUCCESSFULLY,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_400, description = INVALID_DATE_RANGE_INPUT,
                    content = @Content),
            @ApiResponse(responseCode = RESPONSE_CODE_500, description = INTERNAL_SERVER_ERROR,
                    content = @Content)})
    public ResponseEntity<?> searchUsersByBirthDateRange(
            @RequestParam LocalDate from, @RequestParam LocalDate to) {
        log.info("Received request to search users by birth date range from: {}, to: {}", from, to);
        userDtoValidator.validateDateRange(from, to);
        List<UserDto> users = userService.searchUsersByBirthDateRange(from, to);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}

