package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeception.model.NotFoundExeception;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImp implements ItemService {
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserService userService;

    @Override
    public ItemDto addItem(int userId, Item item) {
        userService.getUser(userId);
        item.setOwner(userId);
        return ItemDtoMapper.mapToItemDto(itemStorage.addItem(item));
    }

    @Override
    public ItemDto updateItem(int userId, int itemId, Item item) {
        Item updateItem = itemStorage.getItem(itemId);
        userService.getUser(userId);
        validateOwner(userId, updateItem);
        if (item.getName() != null)
            updateItem.setName(item.getName());
        if (item.getDescription() != null)
            updateItem.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            updateItem.setAvailable(item.getAvailable());
        return ItemDtoMapper.mapToItemDto(itemStorage.updateItem(updateItem));
    }

    @Override
    public ItemDto getItem(int userId, int id) {
        Item item = itemStorage.getItem(id);
        validateOwner(userId, item);
        return ItemDtoMapper.mapToItemDto(item);
    }


    @Override
    public List<ItemDto> getItems(int userId) {
        return itemStorage.getItems().stream()
                .filter(item -> item.getOwner() == userId)
                .map(ItemDtoMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isEmpty())
            return new ArrayList<>();
        return itemStorage.getItems().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .map(ItemDtoMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void validateOwner(int userId, Item item) {
        if (item.getOwner() != userId)
            throw new NotFoundExeception("Owner is incorrect.");
    }
}
