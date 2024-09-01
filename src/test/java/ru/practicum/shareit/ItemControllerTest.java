package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserInMemoryStorage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemController itemController;

    @Autowired
    private UserInMemoryStorage userInMemoryStorage;

    @Test
    void testCreateItem() throws Exception {
        User user = new User();
        user.setName("Ivan");
        user.setEmail("ivan@mail.ru");
        userInMemoryStorage.addUser(user);

        String itemJson = "{\n" +
                "  \"name\": \"Drel\",\n" +
                "  \"description\": \"Drel manual\",\n" +
                "  \"available\": \"true\"\n" +
                "}";

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(itemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Drel manual"));
    }

    @Test
    void testCreateItemFailName() throws Exception {
        String itemJson = "{\n" +
                "  \"name\": \"\",\n" +
                "  \"description\": \"Drel manual\",\n" +
                "  \"available\": \"true\"\n" +
                "}";

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateItemFailDescription() throws Exception {
        String itemJson = "{\n" +
                "  \"name\": \"Drel\",\n" +
                "  \"description\": \"\",\n" +
                "  \"available\": \"true\"\n" +
                "}";

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isBadRequest());
    }

}