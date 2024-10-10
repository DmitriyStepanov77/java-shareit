package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingStatusTest {
    private BookingDtoOut bookingDto;

    @BeforeEach
    public void setup() {
        bookingDto = new BookingDtoOut();
    }

    @Test
    void testWaiting() {
        bookingDto.setStatus(BookingStatus.WAITING);
        assertEquals(bookingDto.getStatus(), BookingStatus.WAITING);
    }

    @Test
    void testApproved() {
        bookingDto.setStatus(BookingStatus.APPROVED);
        assertEquals(bookingDto.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    void testCanceled() {
        bookingDto.setStatus(BookingStatus.CANCELED);
        assertEquals(bookingDto.getStatus(), BookingStatus.CANCELED);
    }

    @Test
    void testRejected() {
        bookingDto.setStatus(BookingStatus.REJECTED);
        assertEquals(bookingDto.getStatus(), BookingStatus.REJECTED);
    }
}
