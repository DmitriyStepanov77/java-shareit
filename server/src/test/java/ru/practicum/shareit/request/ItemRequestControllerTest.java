package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"db.name=test"})
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private User user;
    private Item item;

    @BeforeEach
    void setup() {
        LocalDateTime currentTime = LocalDateTime.now();

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

        itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setRequester(user);
        itemRequest.setCreated(currentTime);
        itemRequest.setDescription("Give me item");

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setRequester(user);
        itemRequestDto.setCreated(currentTime);
        itemRequestDto.setDescription("Give me item");
    }

    @Test
    void addItemRequestTest() throws Exception {
        when(itemRequestService.addRequest(anyInt(), any())).thenReturn(itemRequest);

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId())))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
        verify(itemRequestService, times(1)).addRequest(anyInt(), any());
    }

    @Test
    void getItemRequestTest() throws Exception {
        when(itemRequestService.getRequest(anyInt())).thenReturn(itemRequest);

        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId())))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
        verify(itemRequestService, times(1)).getRequest(anyInt());
    }

    @Test
    void getItemRequestByRequesterTest() throws Exception {
        when(itemRequestService.getAllRequestByRequester(anyInt())).thenReturn(List.of(itemRequest));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId())))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())));
        verify(itemRequestService, times(1)).getAllRequestByRequester(anyInt());
    }
}