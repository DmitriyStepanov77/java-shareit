
package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ItemRequestClientTest {

    private ItemRequestClient itemRequestClient;
    private MockRestServiceServer mockServer;
    private ItemRequestDto itemRequestDto;
    private CommentDto commentDto;

    @BeforeEach
    public void setUp() {
        itemRequestClient = new ItemRequestClient("http:/", new RestTemplateBuilder());
        RestTemplate restTemplate = itemRequestClient.rest;
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetSuccessTest() {
        String path = "http://requests/1";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemRequestClient.get(1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testGetAllSuccessTest() {
        String path = "http://requests";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemRequestClient.getByRequester(1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPostSuccessTest() {
        String path = "http://requests";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemRequestClient.add(itemRequestDto, 1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }
}
