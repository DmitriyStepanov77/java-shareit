package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.user.mapper.UserDtoMapper;

public class BookingDtoMapper {
    public static BookingDtoOut mapToBookingDtoOut(Booking booking) {
        BookingDtoOut bookingDto = new BookingDtoOut();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(ItemDtoMapper.mapToItemDto(booking.getItem()));
        bookingDto.setBooker(UserDtoMapper.mapToUserDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static Booking mapToBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }
}