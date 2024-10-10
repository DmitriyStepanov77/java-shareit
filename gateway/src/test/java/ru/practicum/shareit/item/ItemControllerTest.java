package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mockMvc;
    private ItemDtoOut itemDtoOut;
    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setup() {
        itemDto = new ItemDto();
        itemDto.setOwner(1);
        itemDto.setDescription("Item 1 desc.");
        itemDto.setName("Item 1");
        itemDto.setAvailable(true);

        itemDtoOut = new ItemDtoOut();
        itemDtoOut.setName("Item 1");
        itemDtoOut.setDescription("Item 1 desc.");
        itemDtoOut.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setText("Good");
    }

    @Test
    void addItemTest() throws Exception {
        String itemJson = objectMapper.writeValueAsString(itemDto);
        ResponseEntity<Object> response = new ResponseEntity<>(itemJson, HttpStatus.OK);

        when(itemClient.add(any(), anyInt())).thenReturn(response);

        String content = mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(itemJson, content);

        verify(itemClient).add(any(), anyInt());
    }

    @Test
    void updateItemTest() throws Exception {
        String itemJson = objectMapper.writeValueAsString(itemDto);
        ResponseEntity<Object> response = new ResponseEntity<>(itemJson, HttpStatus.OK);

        when(itemClient.update(itemDto, 1, 1)).thenReturn(response);

        String content = mockMvc.perform(patch("/items/{Id}", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(itemJson, content);

        verify(itemClient).update(itemDto, 1, 1);
    }

    @Test
    void getItemTest() throws Exception {
        mockMvc.perform(get("/items/{Id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).get(anyInt(), anyInt());
    }

    @Test
    void getAllItemTest() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).getAll(anyInt());
    }

    @Test
    void searchItemTest() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "balalaika"))
                .andExpect(status().isOk());

        verify(itemClient).search(any());
    }

    @Test
    void addCommentTest() throws Exception {
        String commentJson = objectMapper.writeValueAsString(commentDto);
        ResponseEntity<Object> response = new ResponseEntity<>(commentJson, HttpStatus.OK);

        when(itemClient.addComment(anyInt(), anyInt(), any())).thenReturn(response);

        String content = mockMvc.perform(post("/items/{id}/comment", 1)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(commentJson, content);

        verify(itemClient).addComment(anyInt(), anyInt(), any());
    }
}