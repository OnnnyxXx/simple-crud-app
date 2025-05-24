package com.example.demo;

import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        savedUser = new User();
        savedUser.setLogin("Test");
        savedUser.setEmail("test@gmail.com");
        savedUser.setPassword("7474712:L");
        savedUser.setFirstName("Testi");
        savedUser.setLastName("Fresti");
        userRepository.save(savedUser);
    }

    @Test
    public void create() throws Exception {
        String userJson = """
                {
                    "login": "NewTest",
                    "email": "newtest@gmail.com",
                    "password": "7474712:L",
                    "firstName": "New",
                    "lastName": "User "
                }""";

        mockMvc.perform(post("/api/v1/users/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void patch() throws Exception {
        String patchNode = """
                {
                    "firstName": "Lofl",
                    "lastName": "Dofi",
                    "email": "t@gmail.com"
                }""";

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/update/" + savedUser.getId())
                        .content(patchNode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/api/v1/users/all"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findByName() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + savedUser.getFirstName()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findByEmail() throws Exception {
        mockMvc.perform(get("/api/v1/users/email/" + savedUser.getEmail()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
