package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {
    private int id;
    private String description;
    private UserDto requester;
    private LocalDateTime created;
    private List<ItemDto> items;
}
