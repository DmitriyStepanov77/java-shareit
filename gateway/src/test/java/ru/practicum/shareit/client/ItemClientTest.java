
package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ItemClientTest {

    private ItemClient itemClient;
    private MockRestServiceServer mockServer;
    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    public void setUp() {
        itemClient = new ItemClient("http:/", new RestTemplateBuilder());
        RestTemplate restTemplate = itemClient.rest;
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetSuccessTest() {
        String path = "http://items/1";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.get(1, 1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testGetAllSuccessTest() {
        String path = "http://items";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.getAll(1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPostSuccessTest() {
        String path = "http://items";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.add(itemDto, 1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPostCommentSuccessTest() {
        String path = "http://items/1/comment";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.addComment(1, 1, commentDto);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testUpdateSuccessTest() {
        String path = "http://items/1";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.update(itemDto, 1, 1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testSearchSuccessTest() {
        String path = "http://items/search?text=1";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.search("1");
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }
}
