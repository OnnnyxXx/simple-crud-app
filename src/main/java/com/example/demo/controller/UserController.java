package com.example.demo.controller;

import com.example.demo.repository.User;
import com.example.demo.repository.UserDto;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @PutMapping("/update/{id}")
    public void update(@PathVariable Long id,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String name) {

        userService.update(id, email, name);

    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
