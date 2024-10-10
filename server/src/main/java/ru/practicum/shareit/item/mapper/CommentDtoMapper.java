package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.Comment;

public class CommentDtoMapper {
    public static CommentDtoOut mapToCommentDto(Comment comment) {
        CommentDtoOut commentDto = new CommentDtoOut();
        commentDto.setId(comment.getId());
        commentDto.setItemId(comment.getItem().getId());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }
}