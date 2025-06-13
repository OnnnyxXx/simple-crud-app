package com.example.demo.controller;

import com.example.demo.repository.User;
import com.example.demo.repository.UserDto;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "User", description = "API для User")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать пользователя", description = "В ответе возвращается объект User")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @Operation(summary = "Получить всех пользователя", description = "В ответе возвращается все объекты User")
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAll() {
        return userService.getAll();
    }

    @Operation(summary = "Получить по имени пользователя", description = "В ответе возвращается объекты User")
    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@PathVariable("name") String firstName) {
        return userService.findByName(firstName);
    }

    @Operation(summary = "Получить по email пользователя", description = "В ответе возвращается объект User")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    @Operation(summary = "Обновить пользователя", description = "В ответе возвращается объект User")
    @PatchMapping("/update/{id}")
    public User patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        return userService.update(id, patchNode);
    }

    @Operation(summary = "Удалить пользователя", description = "В ответе возвращается")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
