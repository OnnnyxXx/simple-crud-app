package com.example.demo.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", indexes = {
        @Index(name = "idx_users_login", columnList = "login")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Login Обязателен")
    @Size(min = 4, max = 100, message = "Login должен быть от 4 до 100 символов")
    private String login;

    @NotBlank(message = "Email Обязателен")
    @Email(message = "Email Не валидный")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password Обязателен")
    @Size(min = 6, max = 100, message = "Password должен быть от 6 до 100 символов")
    private String password;

    @NotBlank(message = "First Name Обязателен")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last Name Обязателен")
    @Size(max = 50)
    private String lastName;
}
