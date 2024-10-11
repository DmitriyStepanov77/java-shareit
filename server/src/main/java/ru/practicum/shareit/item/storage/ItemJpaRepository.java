package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Integer> {

    /**
     * Поиск вещей пользователя.
     *
     * @param ownerId Идентификатор владельца вещи.
     * @return список объектов, содержащий данные о вещах пользователя.
     */
    public List<Item> findByOwnerId(int ownerId);
}
