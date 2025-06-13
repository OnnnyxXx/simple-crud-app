package com.example.demo.auth;

import com.example.demo.auth.jwt.*;
import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    @Value("${jwt.secret.access}")
    String jwtAccessSecret;

    private final UserRepository userRepository;

    public AuthService(UserService userService, JwtProvider jwtProvider, UserRepository userRepository) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final User user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь не найден"));

        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public void logout(HttpServletResponse response) {
        jakarta.servlet.http.Cookie accessCookie = new jakarta.servlet.http.Cookie("accessToken", null);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setValue(null);
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        jakarta.servlet.http.Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        accessCookie.setValue(null);
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));

                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public User getAuthInfo(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtAccessSecret)
                    .setAllowedClockSkewSeconds(60)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userLogin = claims.get("login", String.class);

            Optional<User> user = userRepository.getByLogin(userLogin);
            if (user.isPresent()) return user.get();

        } catch (ExpiredJwtException jwtException) {
            System.out.println("JWT истек: " + jwtException.getMessage());
        } catch (Exception e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
        }
        return null;
    }

}
