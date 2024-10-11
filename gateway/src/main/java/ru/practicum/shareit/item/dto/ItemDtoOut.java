package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemDtoOut extends ItemDto {
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<CommentDtoOut> comments;
}
