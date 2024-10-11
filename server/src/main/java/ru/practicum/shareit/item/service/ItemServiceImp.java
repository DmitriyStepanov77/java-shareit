package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingJpaRepository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentJpaRepository;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(readOnly = true)
public class ItemServiceImp implements ItemService {

    private final ItemJpaRepository itemStorage;
    private final UserService userService;
    private final BookingJpaRepository bookingStorage;
    private final CommentJpaRepository commentStorage;
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemServiceImp(ItemJpaRepository itemStorage, UserService userService, BookingJpaRepository bookingStorage,
                          CommentJpaRepository commentStorage, ItemRequestService itemRequestService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.bookingStorage = bookingStorage;
        this.commentStorage = commentStorage;
        this.itemRequestService = itemRequestService;
    }

    /**
     * Добавление новой вещи.
     *
     * @param userId Идентификатор владельца вещи.
     * @param item   объект, содержащий данные о вещи.
     * @return объект, содержащий данные о созданной вещи.
     */
    @Override
    @Transactional
    public Item addItem(int userId, ItemDto item) {
        Item newItem = new Item();
        newItem.setOwner(userService.getUser(userId));
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        if (item.getRequestId() != 0)
            newItem.setRequest(itemRequestService.getRequest(item.getRequestId()));
        newItem.setAvailable(item.getAvailable());
        Item itemSaved = itemStorage.save(newItem);
        log.info("Added item: id = {}, owner = {}.", itemSaved.getId(), itemSaved.getOwner().getId());
        //return itemStorage.save(itemSaved);
        return itemSaved;
    }

    /**
     * Обновление вещи.
     *
     * @param userId Идентификатор владельца вещи.
     * @param itemId Идентификатор вещи.
     * @param item   объект, содержащий данные о вещи.
     * @return объект, содержащий данные об обновленной вещи.
     * @throws NotFoundException если вещь не найдена.
     */
    @Override
    @Transactional
    public Item updateItem(int userId, int itemId, Item item) {
        Item updateItem = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Error: item " + itemId + " is not found."));
        userService.getUser(userId);
        validateOwner(userId, updateItem);
        if (item.getName() != null)
            updateItem.setName(item.getName());
        if (item.getDescription() != null)
            updateItem.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            updateItem.setAvailable(item.getAvailable());
        log.info("Update item: id = {}, owner = {}.", itemId, userId);
        return itemStorage.save(updateItem);
    }

    /**
     * Получение вещи.
     *
     * @param id Идентификатор вещи.
     * @return объект, содержащий данные о вещи.
     * @throws NotFoundException если вещь не найдена.
     */
    @Override
    public ItemDtoOut getItem(int id) {
        List<Booking> itemsBookings = bookingStorage.findByItemId(id);
        Item item = itemStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Error: item " + id + " is not found."));
        return ItemDtoMapper.mapToItemDtoWithBookingTime(item,
                lastBookingTime(itemsBookings),
                nextBookingTime(itemsBookings),
                commentStorage.findByItemId(item.getId()).stream()
                        .map(CommentDtoMapper::mapToCommentDto)
                        .toList());
    }

    /**
     * Получение всех вещей пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return список объектов, содержащих данные о вещах.
     */
    @Override
    public List<ItemDtoOut> getItems(int userId) {
        List<Item> items = itemStorage.findByOwnerId(userId);
        List<Integer> itemsId = items.stream().map(Item::getId).toList();
        Map<Integer, List<Booking>> itemsBookings = bookingStorage.findByItemIdIn(itemsId).stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId(), Collectors.toList()));
        return items.stream()
                .map(item -> ItemDtoMapper.mapToItemDtoWithBookingTime(item,
                        lastBookingTime(itemsBookings.get(item.getId())),
                        nextBookingTime(itemsBookings.get(item.getId())),
                        commentStorage.findByItemId(item.getId()).stream()
                                .map(CommentDtoMapper::mapToCommentDto)
                                .toList()))
                .toList();
    }

    /**
     * Поиск по всем вещам.
     *
     * @param text Текст поиска.
     * @return список объектов, содержащих данные о вещах.
     */
    @Override
    public List<Item> search(String text) {
        if (text == null || text.isEmpty())
            return new ArrayList<>();
        return itemStorage.findAll().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

    /**
     * Добавление комментария к вещи.
     *
     * @param itemId  Идентификатор вещи.
     * @param userId  Идентификатор пользователя, который добавляет комментарий.
     * @param comment Dto объект, содержащий данные о комментарии.
     * @return объект, содержащих данные о добавленном комментарии.
     */
    @Override
    @Transactional
    public Comment addComment(int itemId, int userId, CommentDto comment) {
        validateBooker(itemId, userId);
        return commentStorage.save(Comment.builder()
                .text(comment.getText())
                .item(itemStorage.findById(itemId)
                        .orElseThrow(() -> new NotFoundException("Error: item " + itemId + " is not found.")))
                .author(userService.getUser(userId))
                .created(LocalDateTime.now())
                .build());
    }

    private void validateOwner(int userId, Item item) {
        if (item.getOwner().getId() != userId)
            throw new NotFoundException("Owner is incorrect.");
    }

    private void validateBooker(int itemId, int userId) {
        bookingStorage.findByItemIdAndBookerIdAndEndBefore(itemId,
                        userId,
                        LocalDateTime.now()).stream()
                .findFirst().orElseThrow(() -> new ValidationException("Error: booker " + userId + " is not found."));
    }

    private LocalDateTime lastBookingTime(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CANCELED)
                .map(Booking::getEnd)
                .filter(end -> end.isBefore(LocalDateTime.now()))
                .reduce((t1, t2) -> t1.isAfter(t2) ? t1 : t2)
                .orElse(null);
    }

    private LocalDateTime nextBookingTime(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.stream()
                .map(Booking::getStart)
                .filter(start -> start.isAfter(LocalDateTime.now()))
                .sorted()
                .findFirst()
                .orElse(null);
    }
}
