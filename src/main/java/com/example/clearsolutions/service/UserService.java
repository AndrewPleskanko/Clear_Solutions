package com.example.clearsolutions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.entity.User;
import com.example.clearsolutions.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final List<User> users = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    /**
     * Creates a new user.
     *
     * @param userDto the user data transfer object
     * @return the created user data transfer object
     */
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        user.setId(counter.incrementAndGet());
        users.add(user);
        log.info("User created: {}", user);
        return userMapper.toUserDto(user);
    }

    /**
     * Updates the fields of an existing user.
     *
     * @param id the id of the user to update
     * @param userDto the user data transfer object with the new values
     * @return the updated user data transfer object
     */
    public UserDto updateUserFields(Long id, UserDto userDto) {
        User existingUser = getUserById(id);

        Optional.ofNullable(userDto.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(userDto.getFirstName()).ifPresent(existingUser::setFirstName);
        Optional.ofNullable(userDto.getLastName()).ifPresent(existingUser::setLastName);
        Optional.ofNullable(userDto.getBirthDate()).ifPresent(existingUser::setBirthDate);
        Optional.ofNullable(userDto.getAddress()).ifPresent(existingUser::setAddress);
        Optional.ofNullable(userDto.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);

        existingUser.setId(id);

        log.info("User fields updated for id: {}, user: {}", id, existingUser);
        return userMapper.toUserDto(existingUser);
    }

    /**
     * Updates an existing user.
     *
     * @param id the id of the user to update
     * @param userDto the user data transfer object with the new values
     * @return the updated user data transfer object
     */
    public UserDto updateUser(Long id, UserDto userDto) {
        deleteUser(id);
        User user = userMapper.toUser(userDto);
        user.setId(id);
        users.add(user);
        log.info("User updated for id: {}, user: {}", id, user);
        return userMapper.toUserDto(user);
    }

    /**
     * Deletes a user.
     *
     * @param id the id of the user to delete
     */
    public void deleteUser(Long id) {
        User user = getUserById(id);
        users.remove(user);
        log.info("User deleted for id: {}", id);
    }

    /**
     * Searches users by birthdate range.
     *
     * @param from the start of the date range
     * @param to the end of the date range
     * @return a list of user data transfer objects that match the date range
     */
    public List<UserDto> searchUsersByBirthDateRange(LocalDate from, LocalDate to) {
        List<User> filteredUsers = users.stream()
                .filter(user -> !user.getBirthDate().isBefore(from) && !user.getBirthDate().isAfter(to))
                .collect(Collectors.toList());
        log.info("Users searched by birth date range from: {}, to: {}, result: {}", from, to, filteredUsers);
        return filteredUsers.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by id.
     *
     * @param id the id of the user to retrieve
     * @return the user
     * @throws IllegalArgumentException if the user is not found
     */
    private User getUserById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("User not found for id: {}", id);
                    return new IllegalArgumentException("User not found");
                });
    }
}