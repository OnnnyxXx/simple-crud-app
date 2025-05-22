package com.example.demo.repository;

import lombok.Value;

/**
 * DTO for {@link User}
 */
@Value
public class UserDto {
    Long id;
    String email;
    String firstName;
    String lastName;

    public UserDto(Long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}