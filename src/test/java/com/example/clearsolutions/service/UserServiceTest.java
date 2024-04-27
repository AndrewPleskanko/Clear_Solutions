package com.example.clearsolutions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.mapper.UserMapper;

@SpringBootTest
public class UserServiceTest {

    private UserService userService;
    private List<UserDto> createdUsers;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userMapper);
        createdUsers = new ArrayList<>();
        createdUsers.add(createTestUser("test3@example.com", "Test3", "User3", LocalDate.now().minusYears(30), "1122334455"));
    }

    @Test
    public void createUser_CreatesNewUser_ReturnsUserWithId() {
        // Given
        UserDto userTest = createTestUser("test5@example.com", "Test5", "User5", LocalDate.now().minusYears(20), "1234567890");

        // When
        UserDto createdUser = userService.createUser(userTest);

        // Then
        assertUserEquals(userTest, createdUser);
    }

    @Test
    public void updateUserFields_UpdatesUserFields_ReturnsUpdatedUser() {
        // Given
        UserDto userToUpdate = createdUsers.get(0);
        UserDto updatedUser = new UserDto();
        updatedUser.setBirthDate(LocalDate.now());

        // When
        userToUpdate = userService.updateUserFields(userToUpdate.getId(), updatedUser);

        // Then
        assertEquals(userToUpdate.getBirthDate(), updatedUser.getBirthDate());
    }

    @Test
    public void updateUser_ReplacesUser_ReturnsUpdatedUser() {
        // Given
        UserDto userToUpdate = createdUsers.get(0);
        UserDto updatedUser = createTestUser("test5@example.com", "Test5", "User5", LocalDate.now().minusYears(20), "1234567890");

        // When
        userToUpdate = userService.updateUser(userToUpdate.getId(), updatedUser);

        // Then
        assertUserEquals(userToUpdate, updatedUser);
    }

    @Test
    public void deleteUser_DeletesUser_ThrowsExceptionWhenUpdatingDeletedUser() {
        // Given
        UserDto userTest = createTestUser("test5@example.com", "Test5", "User5", LocalDate.now().minusYears(20), "1234567890");
        userService.deleteUser(userTest.getId());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.updateUserFields(userTest.getId(), new UserDto()));
    }

    @Test
    public void searchUsersByBirthDateRange_FiltersUsers_ReturnsUsersWithinRange() {
        // Given
        UserDto user1 = createTestUser("test1@example.com", "Test1", "User1", LocalDate.now().minusYears(20), "1234567890");
        UserDto user2 = createTestUser("test2@example.com", "Test2", "User2", LocalDate.now().minusYears(25), "0987654321");

        // When
        List<UserDto> users = userService.searchUsersByBirthDateRange(LocalDate.now().minusYears(22), LocalDate.now().minusYears(18));

        // Then
        assertEquals(1, users.size());
    }

    private UserDto createTestUser(String email, String firstName, String lastName, LocalDate birthDate, String phoneNumber) {
        UserDto user = new UserDto();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(birthDate);
        user.setPhoneNumber(phoneNumber);
        return userService.createUser(user);
    }

    private void assertUserEquals(UserDto expected, UserDto actual) {
        assertNotNull(actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
    }
}