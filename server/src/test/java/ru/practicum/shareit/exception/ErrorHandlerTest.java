package ru.practicum.shareit.exception;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleValidationTest() {
        ValidationException exception = new ValidationException("Validation exception");
        Map<String, String> exceptionText = errorHandler.handleBadRequest(exception);

        assertEquals("Validation exception", exceptionText.get("error"));
    }

    @Test
    void handleNotFoundTest() {
        NotFoundException exception = new NotFoundException("Not found");
        Map<String, String> exceptionText = errorHandler.handleNotFound(exception);

        assertEquals("Not found", exceptionText.get("error"));
    }

    @Test
    void handleConflictTest() {
        ConflictException exception = new ConflictException("Conflict");
        Map<String, String> exceptionText = errorHandler.handleConflict(exception);

        assertEquals("Conflict", exceptionText.get("error"));
    }

    @Test
    void handleForbiddenTest() {
        ForbiddenException exception = new ForbiddenException("Forbidden");
        Map<String, String> exceptionText = errorHandler.handleForbidden(exception);

        assertEquals("Forbidden", exceptionText.get("error"));
    }

}