
package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class BookingClientTest {

    private BookingClient bookingClient;
    private MockRestServiceServer mockServer;
    private BookingDto bookingDto;
    private CommentDto commentDto;

    @BeforeEach
    public void setUp() {
        bookingClient = new BookingClient("http:/", new RestTemplateBuilder());
        RestTemplate restTemplate = bookingClient.rest;
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetSuccessTest() {
        String path = "http://bookings/1";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.get(1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testGetByUserSuccessTest() {
        String path = "http://bookings?state=ALL";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.getByUser(1, "ALL");
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testGetByOwnerSuccessTest() {
        String path = "http://bookings/owner?state=ALL";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.getByOwner(1, "ALL");
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPostSuccessTest() {
        String path = "http://bookings";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.add(1, bookingDto);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testUpdateSuccessTest() {
        String path = "http://bookings/1?approved=true";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.update(1, true, 1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }
}
