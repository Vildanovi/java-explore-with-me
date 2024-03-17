package ru.practicum.stats.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private int id;
    private String text;
    private LocalDateTime created;
    private String authorName;
    private String status;
    private String eventName;
}
