package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    public ItemDto addItem(int userId, Item item);

    public ItemDto updateItem(int userId, int itemId, Item item);

    public ItemDto getItem(int userId, int itemId);

    public List<ItemDto> getItems(int userId);

    public List<ItemDto> search(String text);
}
