package ru.practicum.shareit.exeception.model;

public class NotFoundExeception extends RuntimeException {
    public NotFoundExeception(String e) {
        super(e);
    }
}
