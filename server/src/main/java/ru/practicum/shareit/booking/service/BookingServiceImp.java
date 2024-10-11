package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.storage.BookingJpaRepository;
import ru.practicum.shareit.exception.model.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@Transactional(readOnly = true)
public class BookingServiceImp implements BookingService {

    private final BookingJpaRepository bookingStorage;
    private final ItemJpaRepository itemJpaRepository;
    private final UserService userService;

    @Autowired
    public BookingServiceImp(BookingJpaRepository bookingStorage,
                             ItemJpaRepository itemJpaRepository,
                             UserService userService) {
        this.bookingStorage = bookingStorage;
        this.itemJpaRepository = itemJpaRepository;
        this.userService = userService;
    }

    /**
     * Создание нового бронирования.
     *
     * @param userId     Идентификатор пользователя, создающего бронирование.
     * @param bookingDto DTO объект, содержащий данные о бронировании.
     * @return объект, содержащий данные о созданном бронировании.
     * @throws NotFoundException если вещь не найдена.
     */
    @Override
    @Transactional
    public Booking addBooking(int userId, BookingDto bookingDto) {
        Booking booking = BookingDtoMapper.mapToBooking(bookingDto);
        Item item = itemJpaRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Error: item " + bookingDto.getItemId() + " is not found."));
        if (!item.getAvailable())
            throw new ValidationException();
        booking.setBooker(userService.getUser(userId));
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Booking bookingSaved = bookingStorage.save(booking);
        log.info("Added booking: id = {}, item = {}, booker = {}, start = {}, end = {}.",
                bookingSaved.getId(), bookingSaved.getItem().getId(), bookingSaved.getBooker().getId(),
                bookingSaved.getStart(), bookingSaved.getEnd());
        return bookingSaved;
    }

    /**
     * Подтверждение созданного бронирования.
     *
     * @param userId    Идентификатор владельца вещи, для которой созданно бронирование.
     * @param bookingId Идентификатор бронирования.
     * @param approve   Статус подтверждения бронирования.
     * @return объект, содержащий данные об обновленном бронировании.
     * @throws NotFoundException  если бронирование не найдено.
     * @throws ForbiddenException если пользователь не является владельцем вещи.
     */
    @Override
    @Transactional
    public Booking approveBooking(int userId, int bookingId, boolean approve) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Error: booking is not found."));
        if (booking.getItem().getOwner().getId() == userId)
            booking.setStatus(approve ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        else
            throw new ForbiddenException("Error: owner item is incorrect.");
        log.info("Approve booking: id = {}, status = {}, owner = {}",
                bookingId, booking.getStatus(), userId);
        return bookingStorage.save(booking);
    }

    /**
     * Получение бронирования.
     *
     * @param bookingId Идентификатор бронирования.
     * @return объект, содержащий данные о бронировании.
     * @throws NotFoundException если бронирование не найдено.
     */
    @Override
    @Transactional
    public Booking getBooking(int bookingId) {
        return bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Error: booking is not found."));
    }

    /**
     * Получение списка всех бронирований пользователя.
     *
     * @param bookerId Идентификатор пользователя.
     * @param state    Статус бронирований, которые необходимо получить.
     * @return список объектов, содержащий данные о бронированиях.
     * @throws NotFoundException если статус некорректен.
     */
    @Override
    public List<Booking> getBookingsByUser(int bookerId, String state) {
        userService.getUser(bookerId);
        String a = BookingState.ALL.toString();
        LocalDateTime currentTime = LocalDateTime.now();
        return switch (BookingState.convert(state)) {
            case BookingState.ALL -> bookingStorage.findByBookerIdOrderByStartDesc(bookerId);
            case BookingState.CURRENT -> bookingStorage.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                    currentTime,
                    currentTime);
            case BookingState.PAST -> bookingStorage.findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, currentTime);
            case BookingState.FUTURE -> bookingStorage.findByBookerIdAndStartAfterOrderByStartDesc(bookerId, currentTime);
            case BookingState.WAITING -> bookingStorage.findByBookerIdAndStatusLikeOrderByStartDesc(bookerId,
                    BookingStatus.WAITING);
            case BookingState.REJECTED -> bookingStorage.findByBookerIdAndStatusLikeOrderByStartDesc(bookerId,
                    BookingStatus.REJECTED);
            default -> throw new NotFoundException("Error: state is not valid.");
        };
    }

    /**
     * Получение списка всех бронирований вещей для владельца вещей.
     *
     * @param ownerId Идентификатор пользователя.
     * @param state   Статус бронирований, которые необходимо получить.
     * @return список объектов, содержащий данные о бронированиях.
     * @throws NotFoundException если статус некорректен.
     */
    @Override
    public List<Booking> getBookingsByOwner(int ownerId, String state) {
        userService.getUser(ownerId);
        LocalDateTime currentTime = LocalDateTime.now();
        return switch (BookingState.convert(state)) {
            case BookingState.ALL -> bookingStorage.findByOwnerIdAll(ownerId);
            case BookingState.CURRENT -> bookingStorage.findByOwnerIdCurrent(ownerId, currentTime);
            case BookingState.PAST -> bookingStorage.findByOwnerIdPast(ownerId, currentTime);
            case BookingState.FUTURE -> bookingStorage.findByOwnerIdFuture(ownerId, currentTime);
            case BookingState.WAITING -> bookingStorage.findByOwnerIdStatus(ownerId, BookingStatus.WAITING);
            case BookingState.REJECTED -> bookingStorage.findByOwnerIdStatus(ownerId, BookingStatus.REJECTED);
            default -> throw new NotFoundException("Error: state is not valid.");
        };
    }

}
