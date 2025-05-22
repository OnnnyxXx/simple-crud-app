package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE first_name= :firstName", nativeQuery = true)
    Optional<User> findByName(String firstName);

    @Query(value = "SELECT * FROM users WHERE login= :login", nativeQuery = true)
    Optional<User> getByLogin(String login);
}
