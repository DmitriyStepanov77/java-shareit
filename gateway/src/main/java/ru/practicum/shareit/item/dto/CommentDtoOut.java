package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDtoOut {
    private int id;
    private String text;
    private int itemId;
    private String authorName;
    private LocalDateTime created;
}
