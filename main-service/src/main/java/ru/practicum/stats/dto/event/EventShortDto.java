package ru.practicum.stats.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.constant.Constants;
import ru.practicum.stats.dto.category.CategoryDto;
import ru.practicum.stats.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткая информация о событии")
public class EventShortDto {
    @Schema(example = "1", description = "Идентификатор")
    private Integer id;
    @Schema(example = "Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории",
            description = "Краткое описание")
    private String annotation;
    private CategoryDto category;
    @Schema(example = "5", description = "Количество одобренных заявок на участие в данном событии")
    private int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    @Schema(example = "2024-12-31 15:10:05",
            description = "Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\")")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    @Schema(example = "true", defaultValue = "false", description = "Нужно ли оплачивать участие в событии")
    private boolean paid;
    @Schema(example = "Знаменитое шоу 'Летающая кукуруза'", description = "Заголовок")
    private String title;
    @Schema(example = "999", description = "Количество просмотрев события")
    private Integer views;
}
