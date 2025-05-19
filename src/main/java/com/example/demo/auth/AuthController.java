package com.example.demo.auth;

import com.example.demo.auth.jwt.JwtAuthentication;
import com.example.demo.auth.jwt.JwtRequest;
import com.example.demo.auth.jwt.JwtResponse;
import com.example.demo.auth.jwt.RefreshJwtRequest;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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

    @GetMapping("/info")
    public JwtAuthentication getAuthInfo(Authentication authentication) {
        return (JwtAuthentication) authService.getAuthInfo();
    }

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