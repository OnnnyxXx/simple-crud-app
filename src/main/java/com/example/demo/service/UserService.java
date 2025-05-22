package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.excaption.AppError;
import com.example.demo.repository.UserDto;
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

    public ResponseEntity<?> create(User user) {
        try {
            Optional<UserDto> optionalUser = userRepository.findByEmail(user.getEmail());
            if (optionalUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(optionalUser.get().getEmail() + " уже есть!");
            }
            return ResponseEntity.ok(userRepository.save(user));
        } catch (HttpClientErrorException.Conflict errorException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorException);

        }

    }

    public List<UserDto> userList() {
        return userRepository.findAllUsers();
    }

    public ResponseEntity<?> findByName(String firstName) {
        try {
            List<UserDto> optionalUser = userRepository.findByName(firstName);

            if (optionalUser.isEmpty()) {
                return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                        "Пользователи с -> " + firstName + " не найдены"),
                        HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(optionalUser, HttpStatus.OK);
            }

        } catch (Exception errorException) {
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Произошла ошибка: " + errorException.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> findByEmail(String email) {
        try {
            Optional<UserDto> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                        "Пользователь с -> " + email + " не найден"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception errorException) {
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Произошла ошибка: " + errorException.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.getByLogin(login);
    }

    @Transactional
    public void update(Long id, String email, String name) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Юзера с id: " + id + " не существует");
        }
        User user = optionalUser.get();

        if (email != null && !email.equals(user.getEmail())) {
            Optional<UserDto> foundByEmail = userRepository.findByEmail(email);
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
