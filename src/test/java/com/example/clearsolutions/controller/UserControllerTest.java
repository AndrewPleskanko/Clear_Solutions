package com.example.clearsolutions.controller;

import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.service.UserService;
import com.example.clearsolutions.validator.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserValidator userValidator;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setBirthDate(LocalDate.now().minusYears(20));
        userDto.setPhoneNumber("1234567890");
    }

    @Test
    public void createUser_Returns200() throws Exception {
        // Given
        UserDto newUser = new UserDto();
        newUser.setEmail("test@example.com");
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        newUser.setBirthDate(LocalDate.now().minusYears(20));
        newUser.setPhoneNumber("1234567890");

        // When
        when(userService.createUser(any(UserDto.class))).thenReturn(newUser);

        // Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newUser.getId()))
                .andExpect(jsonPath("$.email").value(newUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(newUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(newUser.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(newUser.getBirthDate().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(newUser.getPhoneNumber()));
    }

    @Test
    public void updateUserFields_Returns200() throws Exception {
        // Given
        UserDto updatedUser = new UserDto();
        updatedUser.setEmail("updated@example.com");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("UpdatedLastName"); // Add this line

        // When
        when(userService.updateUserFields(anyLong(), any(UserDto.class))).thenReturn(updatedUser);

        // Then
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()));
    }

    @Test
    public void updateUser_Returns200() throws Exception {
        // Given
        UserDto updatedUser = new UserDto();
        updatedUser.setEmail("updated@example.com");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setBirthDate(LocalDate.now().minusYears(25));
        updatedUser.setPhoneNumber("0987654321");

        // When
        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(updatedUser);

        // Then
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(updatedUser.getBirthDate().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedUser.getPhoneNumber()));
    }


    @Test
    public void deleteUser_Returns200() throws Exception {
        // Given
        UserDto newUser = new UserDto();
        newUser.setId(1L); // Set the id
        newUser.setEmail("test@example.com");
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        newUser.setBirthDate(LocalDate.now().minusYears(20));
        newUser.setPhoneNumber("1234567890");

        // Create the user
        when(userService.createUser(any(UserDto.class))).thenReturn(newUser);
        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        UserDto createdUser = objectMapper.readValue(response, UserDto.class);

        // Delete the user
        mockMvc.perform(delete("/users/" + createdUser.getId()))
                .andExpect(status().isOk());

        // Check if the user is deleted by searching for users with the same birth date
        mockMvc.perform(get("/users/search")
                        .param("from", createdUser.getBirthDate().toString())
                        .param("to", createdUser.getBirthDate().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void searchUsersByBirthDateRange_Returns200() throws Exception {
        // Given
        LocalDate from = LocalDate.now().minusYears(22);
        LocalDate to = LocalDate.now().minusYears(18);
        List<UserDto> users = Collections.singletonList(userDto);

        // When
        when(userService.searchUsersByBirthDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(users);

        // Then
        mockMvc.perform(get("/users/search")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()))
                .andExpect(jsonPath("$[0].firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(userDto.getLastName()))
                .andExpect(jsonPath("$[0].birthDate").value(userDto.getBirthDate().toString()))
                .andExpect(jsonPath("$[0].phoneNumber").value(userDto.getPhoneNumber()));
    }

    @Test
    public void createUser_UnderAge_Returns400() throws Exception {
        // Given
        UserDto newUser = new UserDto();
        newUser.setEmail("test@example.com");
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        newUser.setBirthDate(LocalDate.now().minusYears(17));
        newUser.setPhoneNumber("1234567890");

        // When
        doThrow(new IllegalArgumentException("User is under 18")).when(userValidator).validateUser(any(UserDto.class));

        // Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }
}