package ru.practicum.shareit.item.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;

@Log4j2
@Repository
public class ItemInMemoryStorage implements ItemStorage {
    private final HashMap<Integer, Item> items = new HashMap<>();
    private Integer id = 0;

    @Override
    public Item addItem(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        log.info("Add item ID = {}, name = {}.", item.getId(), item.getName());
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        log.info("Update item ID = {}, name = {}.", item.getId(), item.getName());
        return item;
    }

    @Override
    public Item getItem(int id) {
        return items.get(id);
    }

    @Override
    public Collection<Item> getItems() {
        return items.values();
    }

    @Override
    public void deleteItem(int id) {
        log.info("Delete item ID = {}.", id);
        items.remove(id);
    }

    private Integer getId() {
        return id++;
    }
}
