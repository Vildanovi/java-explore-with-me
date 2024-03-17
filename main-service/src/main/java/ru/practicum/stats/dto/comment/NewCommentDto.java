package ru.practicum.stats.dto.comment;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewCommentDto {
    @NotBlank(groups = {CreatedBy.class}, message = "Комментарий не может быть пустым")
    @Size(groups = {CreatedBy.class}, max = 1000, message = "Комментарий > 255 символов")
    private String text;
}
