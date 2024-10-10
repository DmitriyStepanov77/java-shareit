package ru.practicum.shareit.exception.model;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String e) {
        super(e);
    }
}
