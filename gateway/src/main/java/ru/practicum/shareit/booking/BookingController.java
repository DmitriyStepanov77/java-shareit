package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                             @RequestBody @Valid BookingDto bookingDto) {
        log.info("Request to add booking from user = {}, item = {}.", userId, bookingDto.getItemId());
        return bookingClient.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @RequestParam boolean approved,
                                                 @PathVariable @Positive int bookingId) {
        log.info("Request to approve booking = {} from owner = {}.", bookingId, userId);
        return bookingClient.update(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable @Positive int bookingId) {
        log.info("Request to get booking = {}.", bookingId);
        return bookingClient.get(bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        log.info("Request to get bookings by user = {}.", userId);
        return bookingClient.getByUser(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        log.info("Request to get bookings by owner = {}.", userId);
        return bookingClient.getByOwner(userId, state);
    }
}