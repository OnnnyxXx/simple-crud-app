package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void create() throws Exception {
        String user = """
                {
                    "login": "Test",
                    "email": "test@gmail.com",
                    "password": "7474712:L",
                    "firstName": "Testi",
                    "lastName": "Fresti"
                }""";

        mockMvc.perform(post("/api/v1/users/create")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(put("/api/v1/users/update/1")
                        .param("email", "qq@gmail.com")
                        .param("name", "TTT"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findByName() throws Exception {
        mockMvc.perform(get("/api/v1/users/TTT"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findByEmail() throws Exception {
        mockMvc.perform(get("/api/v1/users/email/qq@gmail.com"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
