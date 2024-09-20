package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    public Booking addBooking(int userId, BookingDto booking);

    public Booking approveBooking(int userId, int bookingId, boolean approve);

    public Booking getBooking(int bookingId);

    public List<Booking> getBookingsByBooker(int bookerId, String state);

    public List<Booking> getBookingsByOwner(int ownerId, String state);
}
