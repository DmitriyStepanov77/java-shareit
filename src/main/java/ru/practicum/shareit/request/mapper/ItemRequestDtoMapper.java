package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public class ItemRequestDtoMapper {
    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setRequester(itemRequest.getRequester());
        List<Item> items = itemRequest.getItems();
        if(items != null && !items.isEmpty())
            itemRequestDto.setItems(items.stream().map(ItemDtoMapper ::mapToItemDto).toList());
        else
            itemRequestDto.setItems(null);
        return itemRequestDto;
    }
}
