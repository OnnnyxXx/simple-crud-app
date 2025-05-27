package com.example.demo.controller;

import com.example.demo.repository.User;
import com.example.demo.repository.UserDto;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/all")
    public PagedModel<UserDto> getAll(Pageable pageable) {
        return userService.getAll(pageable);
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
