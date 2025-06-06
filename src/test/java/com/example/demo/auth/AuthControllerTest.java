package com.example.demo.auth;

import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link AuthController}
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
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
                .andExpect(cookie().exists("accessToken"))
                .andDo(print());
    }

    @Test
    @Order(2)
    public void getAuthInfo() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .content("""
                        {
                            "login": "Test",
                            "password": "7474712:L"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Cookie[] cookies = loginResult.getResponse().getCookies();

        mockMvc.perform(get("/api/auth/info")
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

