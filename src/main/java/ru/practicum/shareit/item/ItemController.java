package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Обработчик POST-запроса для создания новой вещи.
     *
     * @param userId Идентификатор владельца вещи.
     * @param item   объект, содержащий данные о вещи.
     * @return DTO объект, содержащий данные о созданной вещи.
     */
    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody Item item) {
        return ItemDtoMapper.mapToItemDto(itemService.addItem(userId, item));
    }

    /**
     * Обработчик PATCH-запроса для обновления вещи.
     *
     * @param userId Идентификатор владельца вещи.
     * @param itemId Идентификатор вещи.
     * @param item   объект, содержащий данные о вещи.
     * @return DTO объект, содержащий данные об обновленной вещи.
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId,
                              @RequestBody Item item) {
        return ItemDtoMapper.mapToItemDto(itemService.updateItem(userId, itemId, item));
    }

    /**
     * Обработчик GET-запроса для получения вещи.
     *
     * @param itemId Идентификатор вещи.
     * @return DTO объект, содержащий данные о вещи.
     */
    @GetMapping("/{itemId}")
    public ItemDtoOut getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }

    /**
     * Обработчик GET-запроса для получения списка вещей пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return список DTO объектов, содержащих данные о вещах.
     */
    @GetMapping()
    public List<ItemDtoOut> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItems(userId);
    }

    /**
     * Обработчик GET-запроса для поиска вещей.
     *
     * @param text Текст поиска.
     * @return список DTO объектов, содержащих данные о вещах.
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text).stream().map(ItemDtoMapper::mapToItemDto).collect(Collectors.toList());
    }

    /**
     * Обработчик POST-запроса для добавления комментария к вещи.
     *
     * @param userId  Идентификатор пользователя, оставляющего комментарий.
     * @param itemId  Идентификатор вещи.
     * @param comment Dto объект, содержащий данные о комментарии.
     * @return DTO объект, содержащий данные о созданном комментарии.
     */
    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @PathVariable int itemId,
                                    @RequestBody CommentDto comment) {
        return CommentDtoMapper.mapToCommentDto(itemService.addComment(itemId, userId, comment));
    }

}
