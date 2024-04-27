package com.example.clearsolutions.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.entity.User;

/**
 * Mapper interface for converting between User and UserDto objects.
 */
//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user the User entity
     * @return the UserDto
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    UserDto toUserDto(User user);

    /**
     * Converts a UserDto to a User entity.
     *
     * @param userDto the UserDto
     * @return the User entity
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    User toUser(UserDto userDto);
}