package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"db.name=test"})
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private BookingDto bookingDto;
    private Booking booking;
    private User user;
    private Item item;

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
    void addBookingTest() throws Exception {
        when(bookingService.addBooking(anyInt(), any())).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItemId())));

        verify(bookingService, times(1)).addBooking(anyInt(), any());
    }

    @Test
    void approveBookingTest() throws Exception {
        when(bookingService.approveBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(booking);

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItemId())));

        verify(bookingService, times(1)).approveBooking(anyInt(), anyInt(), anyBoolean());
    }

    @Test
    void getBookingTest() throws Exception {
        when(bookingService.getBooking(anyInt())).thenReturn(booking);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItemId())));

        verify(bookingService, times(1)).getBooking(anyInt());
    }

    @Test
    void getBookingsByUser() throws Exception {
        when(bookingService.getBookingsByUser(anyInt(), anyString())).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItemId())));

        verify(bookingService, times(1)).getBookingsByUser(anyInt(), anyString());
    }

    @Test
    void getBookingsByOwner() throws Exception {
        when(bookingService.getBookingsByOwner(anyInt(), anyString())).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItemId())));
        verify(bookingService, times(1)).getBookingsByOwner(anyInt(), anyString());
    }
}