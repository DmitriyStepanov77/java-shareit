package ru.practicum.shareit.exeception.model;

public class ConflictExeception extends RuntimeException {
    public ConflictExeception(String e) {
        super(e);
    }
}
