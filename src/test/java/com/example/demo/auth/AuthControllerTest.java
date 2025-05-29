package com.example.demo.auth;

import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link AuthController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // Очистить базу данных перед каждым тестом
        userRepository.deleteAll();

        // Создать тестового пользователя
        User user = new User();
        user.setLogin("Test");
        user.setEmail("testi@gmail.com");
        user.setPassword("7474712:L");
        user.setFirstName("Testi");
        user.setLastName("Fresti");
        userRepository.save(user);
    }

    @Test
    public void login() throws Exception {
        String authRequest = """
                {
                    "login": "Test",
                    "password": "7474712:L"
                }""";

        mockMvc.perform(post("/api/auth/login")
                        .content(authRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getNewAccessToken() throws Exception {
        String request = """
                {
                    "refreshToken": "zL1HB3Pch05Avfynovxrf/kpF9O2m4NCWKJUjEp27s9J2jEG3ifiKCGylaZ8fDeoONSTJP/wAzKawB8F9rOMNg=="
                }""";

        mockMvc.perform(post("/api/auth/token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

