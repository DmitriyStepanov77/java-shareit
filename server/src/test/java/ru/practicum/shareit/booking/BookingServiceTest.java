package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImp;
import ru.practicum.shareit.booking.storage.BookingJpaRepository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class BookingServiceTest {

    @InjectMocks
    private BookingServiceImp bookingService;

    @Mock
    private ItemJpaRepository itemStorage;

    @Mock
    private BookingJpaRepository bookingStorage;

    @Mock
    private UserServiceImp userService;

    private User user;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setup() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        user = new User();
        user.setId(1);
        user.setName("Jhon");
        user.setEmail("jhon@mail.ru");

        item = new Item();
        item.setId(1);
        item.setDescription("Item 1 desc.");
        item.setName("Item 1");
        item.setAvailable(true);
        item.setOwner(user);

        booking = new Booking();
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(start);
        booking.setEnd(end);

        bookingDto = new BookingDto();
        bookingDto.setItemId(1);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
    }

    @Test
    void addBookingTest() {
        Mockito
                .when(bookingStorage.save(any()))
                .thenReturn(booking);
        Mockito
                .when(itemStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);

        Booking bookingSaved = bookingService.addBooking(1, bookingDto);
        Mockito.verify(bookingStorage).save(any());

        assertEquals(bookingDto.getItemId(), bookingSaved.getItem().getId());

        verify(bookingStorage, atMostOnce()).save(any());
    }

    @Test
    void getBookingTest() {
        Mockito
                .when(bookingStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(booking));

        Booking bookingGet = bookingService.getBooking(1);
        Mockito.verify(bookingStorage).findById(anyInt());

        assertEquals(bookingGet.getId(), booking.getId());

        verify(bookingStorage, atMostOnce()).findById(anyInt());
    }

    @Test
    void getBookingFailTest() {
        Mockito
                .when(bookingStorage.findById(anyInt()))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,() -> bookingService.getBooking(1));
    }

    @Test
    void getBookingsByUserTest() {
        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);
        Mockito
                .when(bookingStorage.findByBookerIdOrderByStartDesc(anyInt()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingStorage.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingStorage.findByBookerIdAndEndBeforeOrderByStartDesc(anyInt(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingStorage.findByBookerIdAndStartAfterOrderByStartDesc(anyInt(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingStorage.findByBookerIdAndStatusLikeOrderByStartDesc(anyInt(), any()))
                .thenReturn(List.of(booking));

        List<Booking> bookingsGetAll = bookingService.getBookingsByUser(1, "ALL");
        Mockito.verify(bookingStorage).findByBookerIdOrderByStartDesc(anyInt());
        assertEquals(1, bookingsGetAll.size());
        verify(bookingStorage, atMostOnce()).findByBookerIdOrderByStartDesc(anyInt());

        List<Booking> bookingsGetCurrent = bookingService.getBookingsByUser(1, "CURRENT");
        Mockito.verify(bookingStorage).findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(),
                any(), any());
        assertEquals(1, bookingsGetCurrent.size());
        verify(bookingStorage, atMostOnce()).findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(),
                any(), any());

        List<Booking> bookingsGetPast = bookingService.getBookingsByUser(1, "PAST");
        Mockito.verify(bookingStorage).findByBookerIdAndEndBeforeOrderByStartDesc(anyInt(),
                any());
        assertEquals(1, bookingsGetPast.size());
        verify(bookingStorage, atMostOnce()).findByBookerIdAndEndBeforeOrderByStartDesc(anyInt(),
                any());

        List<Booking> bookingsGetFuture = bookingService.getBookingsByUser(1, "FUTURE");
        Mockito.verify(bookingStorage).findByBookerIdAndStartAfterOrderByStartDesc(anyInt(),
                any());
        assertEquals(1, bookingsGetFuture.size());
        verify(bookingStorage, atMostOnce()).findByBookerIdAndStartAfterOrderByStartDesc(anyInt(),
                any());

        List<Booking> bookingsGetWaiting = bookingService.getBookingsByUser(1, "WAITING");
        Mockito.verify(bookingStorage).findByBookerIdAndStatusLikeOrderByStartDesc(anyInt(),
                any());
        assertEquals(1, bookingsGetWaiting.size());
        verify(bookingStorage, atMostOnce()).findByBookerIdAndStatusLikeOrderByStartDesc(anyInt(),
                any());

        List<Booking> bookingsGetRejected = bookingService.getBookingsByUser(1, "REJECTED");
        assertEquals(1, bookingsGetRejected.size());
        verify(bookingStorage, times(2)).findByBookerIdAndStatusLikeOrderByStartDesc(anyInt(),
                any());
    }

    @Test
    void getBookingsByOwnerTest() {
        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);
        Mockito
                .when(bookingStorage.findByOwnerIdAll(anyInt()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingStorage.findByOwnerIdCurrent(anyInt(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingStorage.findByOwnerIdPast(anyInt(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingStorage.findByOwnerIdFuture(anyInt(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingStorage.findByOwnerIdStatus(anyInt(), any()))
                .thenReturn(List.of(booking));

        List<Booking> bookingsGetAll = bookingService.getBookingsByOwner(1, "ALL");
        Mockito.verify(bookingStorage).findByOwnerIdAll(anyInt());
        assertEquals(1, bookingsGetAll.size());
        verify(bookingStorage, atMostOnce()).findByOwnerIdAll(anyInt());

        List<Booking> bookingsGetCurrent = bookingService.getBookingsByOwner(1, "CURRENT");
        Mockito.verify(bookingStorage).findByOwnerIdCurrent(anyInt(), any());
        assertEquals(1, bookingsGetCurrent.size());
        verify(bookingStorage, atMostOnce()).findByOwnerIdCurrent(anyInt(), any());

        List<Booking> bookingsGetPast = bookingService.getBookingsByOwner(1, "PAST");
        Mockito.verify(bookingStorage).findByOwnerIdPast(anyInt(), any());
        assertEquals(1, bookingsGetPast.size());
        verify(bookingStorage, atMostOnce()).findByOwnerIdPast(anyInt(), any());

        List<Booking> bookingsGetFuture = bookingService.getBookingsByOwner(1, "FUTURE");
        Mockito.verify(bookingStorage).findByOwnerIdFuture(anyInt(), any());
        assertEquals(1, bookingsGetFuture.size());
        verify(bookingStorage, atMostOnce()).findByOwnerIdFuture(anyInt(), any());

        List<Booking> bookingsGetWaiting = bookingService.getBookingsByOwner(1, "WAITING");
        Mockito.verify(bookingStorage).findByOwnerIdStatus(anyInt(), any());
        assertEquals(1, bookingsGetWaiting.size());
        verify(bookingStorage, atMostOnce()).findByOwnerIdStatus(anyInt(), any());

        List<Booking> bookingsGetRejected = bookingService.getBookingsByOwner(1, "REJECTED");
        assertEquals(1, bookingsGetRejected.size());
        verify(bookingStorage, times(2)).findByOwnerIdStatus(anyInt(), any());
    }


    @Test
    void approveBookingTest() {
        Mockito
                .when(bookingStorage.save(any()))
                .thenReturn(booking);
        Mockito
                .when(bookingStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(booking));

        Booking bookingGet = bookingService.approveBooking(1, 1, true);
        Mockito.verify(bookingStorage).save(any());

        assertEquals(bookingGet.getStatus(), BookingStatus.APPROVED);

        verify(bookingStorage, times(1)).save(any());
    }
}