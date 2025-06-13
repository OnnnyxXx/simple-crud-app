package com.example.demo.auth;

import com.example.demo.auth.jwt.JwtRequest;
import com.example.demo.auth.jwt.JwtResponse;
import com.example.demo.auth.jwt.RefreshJwtRequest;
import com.example.demo.repository.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "API для Регистрации и Авторизации")
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Вход в систему", description = "В ответе возвращается статус и в cookie сохраняется jwt")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest, HttpServletResponse response) throws AuthException {
        final JwtResponse token = authService.login(authRequest);

        Cookie accessCookie = new Cookie("accessToken", token.getAccessToken());
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60); // 1 час
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refreshToken", token.getRefreshToken());
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24); // 1 день
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        authService.logout(response);
    }

    @Operation(summary = "Информация об авторизированным пользователе",
            description = "В ответе возвращается информация о пользователе, на основе jwt")
    @GetMapping("/info")
    public User getAuthInfo(HttpServletRequest httpServletRequest) {
        String token = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        if (token == null) return null;

        return authService.getAuthInfo(token);
    }

    @Operation(summary = "Получения токена",
            description = "В ответе возвращается jwt")
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}