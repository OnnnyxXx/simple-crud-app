package com.example.demo.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Login Обязателен")
    @Size(min = 4, max = 100)
    private String login;

    @NotBlank(message = "Email Обязателен")
    @Email(message = "Email Не валидный")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password Обязателен")
    @Size(min = 6, max = 100)
    private String password;

    private String firstName;
    private String lastName;

    public User() {
    }

    public User(Long id, String login, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}