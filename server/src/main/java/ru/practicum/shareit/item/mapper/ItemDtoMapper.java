package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public class ItemDtoMapper {
    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setName(item.getName());
        itemDto.setOwner(item.getOwner().getId());
        if (item.getRequest() != null)
            itemDto.setRequestId(item.getRequest().getId());
        else
            itemDto.setRequestId(0);
        return itemDto;
    }

    public static ItemDtoOut mapToItemDtoWithBookingTime(Item item, LocalDateTime bookingLastTime,
                                                         LocalDateTime bookingNextTime,
                                                         List<CommentDtoOut> comments) {
        ItemDtoOut itemDto = new ItemDtoOut();
        itemDto.setId(item.getId());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setName(item.getName());
        itemDto.setOwner(item.getOwner().getId());
        if (item.getRequest() != null)
            itemDto.setRequestId(item.getRequest().getId());
        else
            itemDto.setRequestId(0);
        itemDto.setLastBooking(bookingLastTime);
        itemDto.setNextBooking(bookingNextTime);
        itemDto.setComments(comments);
        return itemDto;
    }
}