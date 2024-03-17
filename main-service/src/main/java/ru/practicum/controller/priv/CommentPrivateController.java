package ru.practicum.controller.priv;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.service.CommentService;
import ru.practicum.stats.dto.comment.CommentResponseDto;
import ru.practicum.stats.dto.comment.NewCommentDto;
import ru.practicum.stats.dto.comment.UpdateCommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Добавление комментария")
    public CommentResponseDto postComment(@PathVariable @Positive Integer userId,
                                          @RequestParam (value = "eventId") Integer eventId,
                                          @Validated(CreatedBy.class) @RequestBody NewCommentDto newCommentDto) {
        return CommentMapper.mapCommentToResponseDto(commentService
                .createComment(eventId, userId, newCommentDto));
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "Изменение комментария")
    public CommentResponseDto updateEventByUser(@PathVariable @Positive Integer userId,
                                                @PathVariable @Positive Integer commentId,
                                                @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        return CommentMapper.mapCommentToResponseDto(commentService
                .updateComment(commentId, userId, updateCommentDto));
    }

    @GetMapping
    @Operation(
            summary = "Получение комментариев пользователя",
            description = "В случае, если по заданным фильтрам не найдено ни одного события, возвращает " +
                    "пустой список"
    )
    public List<CommentResponseDto> getAllEventsByUser(@PathVariable @Positive Integer userId,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getAllCommentsByUser(userId, from, size)
                .stream()
                .map(CommentMapper::mapCommentToResponseDto)
                .collect(Collectors.toList());
    }
}
