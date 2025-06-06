package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.demo.excaption.AppError;
import com.example.demo.repository.UserDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserService(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<?> create(User user) {
        try {
            Optional<User> optionalUser = userRepository.getByLogin(user.getLogin());
            if (optionalUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Такой логин уже есть!");
            }
            return ResponseEntity.ok(userRepository.save(user));
        } catch (HttpClientErrorException.Conflict errorException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorException);
        }
    }

    public PagedModel<UserDto> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        Page<UserDto> userDto = users.map(user -> new UserDto(
                user.getId(), user.getEmail(), user.getFirstName(), user.getLastName())
        );
        return new PagedModel<>(userDto);
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
    public User update(Long id, JsonNode patchNode) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с -> `%s` не найден".formatted(id)));

        objectMapper.readerForUpdating(user).readValue(patchNode);

        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
