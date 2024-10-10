
package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class BaseClientTest {

    private BaseClient baseClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        baseClient = new BaseClient(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetSuccessTest() {
        String path = "/test";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = baseClient.get(path, 1);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testGetSuccessWithUserTest() {
        String path = "/test";
        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"success\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = baseClient.get(path);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "success");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPostSuccessTest() {
        String path = "/test";
        Map<String, String> body = new HashMap<>();
        body.put("key", "value");

        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"created\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = baseClient.post(path, body);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "created");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPostSuccessWithUserTest() {
        String path = "/test";
        Map<String, String> body = new HashMap<>();
        body.put("key", "value");

        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"created\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = baseClient.post(path, 1, body);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "created");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPatchSuccessTest() {
        String path = "/test";
        Map<String, String> body = new HashMap<>();
        body.put("key", "updatedValue");

        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"updated\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = baseClient.patch(path, body);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "updated");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPostParametersSuccessTest() {
        String path = "/test";
        Map<String, String> body = new HashMap<>();
        body.put("key", "updatedValue");

        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"updated\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = baseClient.post(path, body.size(), Map.of("key", "updatedValue"), body);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "updated");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testPatchSuccessWithUserTest() {
        String path = "/test";
        Map<String, String> body = new HashMap<>();
        body.put("key", "updatedValue");

        mockServer.expect(requestTo(path))
                .andRespond(withSuccess("{\"message\":\"updated\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = baseClient.patch(path, 1, body);
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "updated");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
        mockServer.verify();
    }

    @Test
    public void testDeleteSuccessTest() {
        String path = "/test";
        mockServer.expect(requestTo(path))
                .andRespond(withStatus(HttpStatus.OK));

        ResponseEntity<Object> response = baseClient.delete(path);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    public void testGetNotFoundTest() {
        String path = "/test";
        mockServer.expect(requestTo(path))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        ResponseEntity<Object> response = baseClient.get(path);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    public void testPostWithErrorTest() {
        String path = "/test";
        Map<String, String> body = new HashMap<>();
        body.put("key", "value");

        mockServer.expect(requestTo(path))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<Object> response = baseClient.post(path, body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        mockServer.verify();
    }
}
