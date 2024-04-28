package com.example.clearsolutions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.clearsolutions.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.entity.User;
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
        createdUsers.add(createTestUser("test1@example.com", "Test1",
                "User1", LocalDate.now().minusYears(20), "9999999999"));

        createdUsers.add(createTestUser("test3@example.com", "Test3",
                "User3", LocalDate.now().minusYears(30), "1122334455"));
    }

    @Test
    public void createUser_CreatesNewUser_ReturnsUserWithId() {
        // Given
        UserDto userTest = createTestUser("test5@example.com", "Test5",
                "User5", LocalDate.now().minusYears(20), "1234567890");

        // When
        UserDto createdUser = userService.createUser(userTest);

        // Then
        assertUserEquals(userTest, createdUser);
    }

    @Test
    public void updateUserFields_UpdatesUserFields_ReturnsUpdatedUser() {
        // Given
        UserDto userToUpdate = createdUsers.get(0);
        User userBeforeUpdate = userMapper.toUser(userToUpdate);
        UserDto updatedUser = new UserDto();
        updatedUser.setBirthDate(LocalDate.now());

        // When
        userToUpdate = userService.updateUserFields(userBeforeUpdate.getId(), updatedUser);

        // Then
        assertEquals(userToUpdate.getBirthDate(), updatedUser.getBirthDate());
    }

    @Test
    public void updateUser_ReplacesUser_ReturnsUpdatedUser() {
        // Given
        UserDto user1 = createdUsers.get(0);
        UserDto user2 = createdUsers.get(1);

        // When
        UserDto updatedUser = userService.updateUser(user1.getId(), user2);

        // Then
        assertUserEquals(updatedUser, user2);
    }

    @Test
    public void deleteUser_DeletesUser_ThrowsExceptionWhenUpdatingDeletedUser() {
        // Given
        User user2 = userMapper.toUser(createdUsers.get(1));
        userService.deleteUser(user2.getId());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.updateUserFields(user2.getId(), new UserDto()));
    }

    @Test
    public void searchUsersByBirthDateRange_FiltersUsers_ReturnsUsersWithinRange() {
        // Given
        UserDto user1 = createTestUser("test1@example.com", "Test1",
                "User1", LocalDate.now().minusYears(20), "1234567890");
        UserDto user2 = createTestUser("test2@example.com", "Test2",
                "User2", LocalDate.now().minusYears(25), "0987654321");

        // When
        List<UserDto> users = userService.searchUsersByBirthDateRange(
                LocalDate.now().minusYears(22), LocalDate.now().minusYears(18));

        // Then
        assertEquals(2, users.size());
    }

    private UserDto createTestUser(String email, String firstName, String lastName,
                                   LocalDate birthDate, String phoneNumber) {
        UserDto user = new UserDto();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(birthDate);
        user.setPhoneNumber(phoneNumber);
        return userService.createUser(user);
    }

    private void assertUserEquals(UserDto expected, UserDto actual) {
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
    }
}