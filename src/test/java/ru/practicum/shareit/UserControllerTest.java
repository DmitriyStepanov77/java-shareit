package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Test
    void testCreateUser() throws Exception {
        String userJsonPost = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"email\": \"adipisicing@mail.ru\"\n" +
                "}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonPost))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("adipisicing@mail.ru"));
    }

    @Test
    void testCreateUserFailEmail() throws Exception {
        String userJson = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"email\": \"adipisicingmail.ru\"\n" +
                "}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUserExtendEmail() throws Exception {
        String userJson1 = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"email\": \"adipisicin@gmail.ru\"\n" +
                "}";

        String userJson2 = "{\n" +
                "  \"name\": \"nisi1 eiusmod1\",\n" +
                "  \"email\": \"adipisicin@gmail.ru\"\n" +
                "}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(status().isConflict());
    }

}