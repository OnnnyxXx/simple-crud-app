package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT id, email, first_name, last_name FROM users WHERE email = :email", nativeQuery = true)
    Optional<UserDto> findByEmail(String email);

    @Query(value = "SELECT id, email, first_name, last_name FROM users WHERE first_name= :firstName", nativeQuery = true)
    List<UserDto> findByName(String firstName);

    @Query(value = "SELECT id, email, first_name, last_name FROM users WHERE login= :login", nativeQuery = true)
    Optional<User> getByLogin(String login);

    @Query(value = "SELECT id, email, first_name, last_name FROM users", nativeQuery = true)
    List<UserDto> findAllUsers();
}
