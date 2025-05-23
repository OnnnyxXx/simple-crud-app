package com.example.demo.controller;

import com.example.demo.repository.User;
import com.example.demo.repository.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    public UserController(UserService userService,
                          UserRepository userRepository,
                          ObjectMapper objectMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping()
    public List<UserDto> findAll() {
        return userService.userList();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@PathVariable("name") String firstName) {
        return userService.findByName(firstName);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    @PatchMapping("/update/{id}")
    public User patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        return userService.update(id, patchNode);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
