package ru.practicum.shareit.exception.model;

public class ConflictException extends RuntimeException {
    public ConflictException(String e) {
        super(e);
    }
}
