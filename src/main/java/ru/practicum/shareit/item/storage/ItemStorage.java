package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    public Item addItem(Item item);

    public Item updateItem(Item item);

    public Item getItem(int id);

    public Collection<Item> getItems();

    public void deleteItem(int id);
}
