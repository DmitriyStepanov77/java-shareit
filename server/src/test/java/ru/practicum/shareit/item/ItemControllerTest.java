package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"db.name=test"})
public class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    private ItemDtoOut itemDtoOut;
    private ItemDto itemDto;
    private Item item;
    private CommentDtoOut commentDtoOut;
    private Comment comment;
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);
        user.setName("Jhon");
        user.setEmail("jhon@mail.ru");

        item = new Item();
        item.setId(1);
        item.setDescription("Item 1 desc.");
        item.setName("Item 1");
        item.setAvailable(true);
        item.setOwner(user);

        itemDto = new ItemDto();
        itemDto.setOwner(1);
        itemDto.setDescription("Item 1 desc.");
        itemDto.setName("Item 1");
        itemDto.setAvailable(true);

        itemDtoOut = new ItemDtoOut();
        itemDtoOut.setId(1);
        itemDtoOut.setName("Item 1");
        itemDtoOut.setDescription("Item 1 desc.");
        itemDtoOut.setAvailable(true);

        commentDtoOut = new CommentDtoOut();
        commentDtoOut.setId(1);
        commentDtoOut.setText("Good");
        commentDtoOut.setAuthorName("Jhon");

        comment = new Comment();
        comment.setId(1);
        comment.setText("Good");
        comment.setItem(item);
        comment.setAuthor(user);
    }

    @Test
    void addItemTest() throws Exception {
        when(itemService.addItem(anyInt(), any())).thenReturn(item);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId())))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable())));

        verify(itemService, times(1)).addItem(anyInt(), any());
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(anyInt(), anyInt(), any())).thenReturn(item);

        mockMvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId())))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable())));

        verify(itemService, times(1)).updateItem(anyInt(),anyInt(), any());
    }

    @Test
    void getItemTest() throws Exception {
       when(itemService.getItem(anyInt())).thenReturn(itemDtoOut);

        mockMvc.perform(get("/items/{Id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId())))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable())));

        verify(itemService, times(1)).getItem(anyInt());
    }

    @Test
    void getAllItemsTest() throws Exception {
        when(itemService.getItems(anyInt())).thenReturn(List.of(itemDtoOut));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDtoOut.getId())))
                .andExpect(jsonPath("$.[0].name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDtoOut.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDtoOut.getAvailable())));

        verify(itemService, times(1)).getItems(anyInt());
    }

    @Test
    void searchItemTest() throws Exception {
        when(itemService.search(anyString())).thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "Item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDtoOut.getId())))
                .andExpect(jsonPath("$.[0].name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDtoOut.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDtoOut.getAvailable())));

        verify(itemService, times(1)).search(anyString());
    }

    @Test
    void addCommentTest() throws Exception {
        when(itemService.addComment(anyInt(), anyInt(), any())).thenReturn(comment);

        mockMvc.perform(post("/items/{id}/comment", 1)
                        .content(mapper.writeValueAsString(commentDtoOut))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDtoOut.getId())))
                .andExpect(jsonPath("$.text", is(commentDtoOut.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDtoOut.getAuthorName())));

        verify(itemService, times(1)).addComment(anyInt(), anyInt(), any());
    }
}