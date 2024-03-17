package ru.practicum.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constant.Constants;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.enumerations.RequestStatus;
import ru.practicum.service.CommentService;
import ru.practicum.stats.dto.comment.CommentResponseDto;
import ru.practicum.stats.dto.comment.UpdateCommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/comments/")
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    @Operation(summary = "Изменение комментария")
    public CommentResponseDto updateEventByUser(@PathVariable @Positive Integer commentId,
                                                @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        return CommentMapper.mapCommentToResponseDto(commentService
                .updateCommentAdmin(commentId, updateCommentDto));
    }

    @GetMapping
    @Operation(
            summary = "Поиск комментариев",
            description = "Эндпоинт возвращает полную информацию обо всех комментариях подходящих " +
                    "под переданные условия"
    )
    public List<CommentResponseDto> getComments(@RequestParam(required = false) List<Integer> users,
                                                @RequestParam(required = false) List<RequestStatus> status,
                                                @RequestParam(required = false) String text,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        return commentService.getComments(users, status, text, rangeStart, rangeEnd, from, size)
                .stream()
                .map(CommentMapper::mapCommentToResponseDto)
                .collect(Collectors.toList());
    }


}
