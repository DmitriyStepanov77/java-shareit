package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemDtoMapperTest {
    private Item item;
    private ItemDto itemDto;
    private User user;
    private ItemRequest itemRequest;
    private CommentDtoOut commentDtoOut;

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

        itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setItems(List.of(item));
        itemRequest.setRequester(user);
        itemRequest.setDescription("Give me item");

        item.setRequest(itemRequest);

        commentDtoOut = new CommentDtoOut();
        commentDtoOut.setText("Good");

    }

    @Test
    void mapToItemDtoTest() {
        itemDto = ItemDtoMapper.mapToItemDto(item);

        assertEquals(itemDto.getRequestId(),itemRequest.getId());
    }

    @Test
    void mapToItemDtoWithBookingTimeTest() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        itemDto = ItemDtoMapper.mapToItemDtoWithBookingTime(item, start, end, List.of(commentDtoOut));

        assertEquals(itemDto.getRequestId(),itemRequest.getId());
    }
}
