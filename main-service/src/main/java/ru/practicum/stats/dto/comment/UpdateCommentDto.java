package ru.practicum.stats.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentDto {
    private String commentState;
    @NotBlank(groups = {CreatedBy.class}, message = "Комментарий не может быть пустым")
    @Size(groups = {CreatedBy.class}, max = 1000, message = "Комментарий > 255 символов")
    private String text;
}
