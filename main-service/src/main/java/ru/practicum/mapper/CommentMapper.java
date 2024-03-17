package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.Comments;
import ru.practicum.stats.dto.comment.CommentResponseDto;
import ru.practicum.stats.dto.comment.NewCommentDto;

@UtilityClass
public class CommentMapper {

    public Comments mapCommentDtoToComment(NewCommentDto newCommentDto) {
        Comments comment = new Comments();
        comment.setText(newCommentDto.getText());
        return comment;
    }

    public CommentResponseDto mapCommentToResponseDto(Comments comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorName(comment.getAuthor().getName())
                .status(comment.getStatus().toString())
                .eventName(comment.getEvent().getTitle())
                .build();
    }
}
