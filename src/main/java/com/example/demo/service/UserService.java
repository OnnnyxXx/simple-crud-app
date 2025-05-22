package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new IllegalStateException("Юзер с таким email уже есть");
        }
        return userRepository.save(user);
    }

    public List<User> userList() {
        return userRepository.findAll();

    }

    public ResponseEntity<?> findByName(String firstName) {
        try {
            Optional<User> optionalUser = userRepository.findByName(firstName);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Пользователь с -> " + firstName + " не найден");
            }
            return ResponseEntity.ok(optionalUser.get());
        } catch (HttpClientErrorException.NotFound errorException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorException);
        }
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.getByLogin(login);
    }

    public ResponseEntity<?> findByEmail(String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Пользователь с -> " + email + " не найден");
            }
            return ResponseEntity.ok(optionalUser.get());
        } catch (HttpClientErrorException.NotFound errorException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorException);
        }
    }

    @Transactional
    public void update(Long id, String email, String name) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Юзера с id: " + id + " не существует");
        }
        User user = optionalUser.get();

        if (email != null && !email.equals(user.getEmail())) {
            Optional<User> foundByEmail = userRepository.findByEmail(email);
            if (foundByEmail.isPresent()) {
                throw new IllegalStateException("Юзер с таким email уже существует");
            }
            user.setEmail(email);
        }

        if (name != null && !name.equals(user.getFirstName())) {
            user.setFirstName(name);
        }
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
