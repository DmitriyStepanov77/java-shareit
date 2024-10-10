
package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class UserClientTest {

    private UserClient userClient;
    private MockRestServiceServer mockServer;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient("http:/", new RestTemplateBuilder());
        RestTemplate restTemplate = userClient.rest;
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetSuccessTest() {
        String path = "http://users/1";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.get(1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPostSuccessTest() {
        String path = "http://users";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.add(userDto);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testDeleteSuccessTest() {
        String path = "http://users/1";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.delete(1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testUpdateSuccessTest() {
        String path = "http://users/1";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.update(userDto, 1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }
}
