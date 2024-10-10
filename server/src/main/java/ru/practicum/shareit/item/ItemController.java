package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ItemDto> addItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @RequestBody ItemDto item) {
        return ResponseEntity.ok()
                .body(ItemDtoMapper.mapToItemDto(itemService.addItem(userId, item)));
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
    public ResponseEntity<ItemDto> updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @PathVariable int itemId,
                                              @RequestBody Item item) {
        return ResponseEntity.ok()
                .body(ItemDtoMapper.mapToItemDto(itemService.updateItem(userId, itemId, item)));
    }

    /**
     * Обработчик GET-запроса для получения вещи.
     *
     * @param itemId Идентификатор вещи.
     * @return DTO объект, содержащий данные о вещи.
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDtoOut> getItem(@PathVariable int itemId) {
        return ResponseEntity.ok()
                .body(itemService.getItem(itemId));
    }

    /**
     * Обработчик GET-запроса для получения списка вещей пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return список DTO объектов, содержащих данные о вещах.
     */
    @GetMapping()
    public ResponseEntity<List<ItemDtoOut>> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return ResponseEntity.ok()
                .body(itemService.getItems(userId));
    }

    /**
     * Обработчик GET-запроса для поиска вещей.
     *
     * @param text Текст поиска.
     * @return список DTO объектов, содержащих данные о вещах.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text) {
        return ResponseEntity.ok()
                .body(itemService.search(text).stream()
                        .map(ItemDtoMapper::mapToItemDto).collect(Collectors.toList()));
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
    public ResponseEntity<CommentDtoOut> addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                                    @PathVariable int itemId,
                                                    @RequestBody CommentDto comment) {
        return ResponseEntity.ok()
                .body(CommentDtoMapper.mapToCommentDto(itemService.addComment(itemId, userId, comment)));
    }

}
