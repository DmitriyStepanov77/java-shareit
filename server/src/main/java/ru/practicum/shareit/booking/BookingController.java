package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Обработчик POST-запроса для создания нового бронирования вещи.
     *
     * @param userId     Идентификатор пользователя, создающего бронирование.
     * @param bookingDto DTO объект, содержащий данные о бронировании.
     * @return DTO объект, содержащий данные о созданном бронировании.
     */
    @PostMapping
    public ResponseEntity<BookingDtoOut> addBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                                    @RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok()
                .body(BookingDtoMapper.mapToBookingDtoOut(bookingService.addBooking(userId, bookingDto)));
    }

    /**
     * Обработчик PATCH-запроса для обновления статуса бронирования.
     *
     * @param userId    Идентификатор владельца вещи.
     * @param bookingId Идентификатор бронирования.
     * @param approved  Статус подтверждения/отклонения бронирования.
     * @return DTO объект, содержащий данные об обновленном бронировании.
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDtoOut> approveBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam boolean approved,
                                                        @PathVariable int bookingId) {
        return ResponseEntity.ok()
                .body(BookingDtoMapper.mapToBookingDtoOut(bookingService.approveBooking(userId, bookingId, approved)));
    }

    /**
     * Обработчик GET-запроса для получения бронирования.
     *
     * @param bookingId Идентификатор бронирования.
     * @return DTO объект, содержащий данные о бронировании.
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDtoOut> getBooking(@PathVariable int bookingId) {
        return ResponseEntity.ok()
                .body(BookingDtoMapper.mapToBookingDtoOut(bookingService.getBooking(bookingId)));
    }

    /**
     * Обработчик GET-запроса для получения списка бронирований пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param state  Состояние получаемых бронирований.
     * @return список DTO объектов, содержащий данные о бронированиях.
     */
    @GetMapping()
    public ResponseEntity<List<BookingDtoOut>> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                 @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok()
                .body(bookingService.getBookingsByUser(userId, state).stream()
                        .map(BookingDtoMapper::mapToBookingDtoOut)
                        .toList());
    }

    /**
     * Обработчик GET-запроса для получения списка бронирований вещей владельца.
     *
     * @param userId Идентификатор владельца.
     * @param state  Состояние получаемых бронирований.
     * @return список DTO объектов, содержащий данные о бронированиях.
     */
    @GetMapping("/owner")
    public ResponseEntity<List<BookingDtoOut>> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                 @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok()
                .body(bookingService.getBookingsByOwner(userId, state).stream()
                        .map(BookingDtoMapper::mapToBookingDtoOut)
                        .toList());
    }
}
