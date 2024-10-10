package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    public Item addItem(int userId, ItemDto item);

    public Item updateItem(int userId, int itemId, Item item);

    public ItemDtoOut getItem(int itemId);

    public List<ItemDtoOut> getItems(int userId);

    public List<Item> search(String text);

    public Comment addComment(int itemId, int userId, CommentDto comment);
}
