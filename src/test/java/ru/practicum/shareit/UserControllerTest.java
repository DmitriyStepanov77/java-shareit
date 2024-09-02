package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    void testGetUser() throws Exception {
        String userJsonPost = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"email\": \"adipisicing@mail.ru\"\n" +
                "}";
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonPost))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("nisi eiusmod"));
    }

    @Test
    void testGetNotFoundUser() throws Exception {
        String userJsonPost = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"email\": \"adipisicing@mail.ru\"\n" +
                "}";

        mockMvc.perform(get("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound());
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

    @Test
    void testUpdateUser() throws Exception {
        String userJson1 = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"email\": \"adipisicin@gmail.ru\"\n" +
                "}";

        String userJsonUpdate = "{\n" +
                "  \"email\": \"adipisicin1@gmail.ru\"\n" +
                "}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("adipisicin1@gmail.ru"));
    }

    @Test
    void testUpdateUserExtendEmail() throws Exception {
        String userJson1 = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"email\": \"adipisicin@gmail.ru\"\n" +
                "}";

        String userJson2 = "{\n" +
                "  \"name\": \"nisi1 eiusmod1\",\n" +
                "  \"email\": \"adipisicin1@gmail.ru\"\n" +
                "}";

        String userJsonUpdate2 = "{\n" +
                "  \"email\": \"adipisicin@gmail.ru\"\n" +
                "}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdate2))
                .andExpect(status().isConflict());
    }

    @Test
    void testDeleteUser() throws Exception {
        String userJson1 = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"email\": \"adipisicin@gmail.ru\"\n" +
                "}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound());
    }

}