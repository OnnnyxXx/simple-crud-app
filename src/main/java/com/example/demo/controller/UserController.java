package com.example.demo.controller;

import com.example.demo.repository.User;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> findAll() {
        return userService.userList();
    }

    @GetMapping("/{name}")
    public User findByName(@PathVariable("name") String name) {
        return userService.findByName(name);
    }

    @GetMapping("/email/{email}")
    public User findByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    @PostMapping("/create")
    public User create(@RequestBody User user) {
        return userService.create(user);

    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable Long id,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String name) {

        userService.update(id, email, name);

    }
}
