package ru.practicum.stats.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.constant.Constants;
import ru.practicum.stats.dto.locations.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Новое событие")
public class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120, message = "title < 3 или >120 симвлов")
    @Schema(example = "Сплав на байдарках",
            description = "Заголовок события")
    private String title;
    @NotBlank
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Size(min = 20, max = 2000, message = "annotation < 20 или >2000 симвлов")
    private String annotation;
    @Positive
    @NotNull
    @Schema(example = "2",
            description = "id категории к которой относится событие")
    private Integer category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "description < 20 или >7000 симвлов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    @Schema(example = "2024-12-31 15:10:05",
            description = "Дата и время на которые намечено событие. " +
                    "Дата и время указываются в формате \"yyyy-MM-dd HH:mm:ss\"")
    private LocalDateTime eventDate;
    @NotNull
    @Valid
    private LocationDto location;
    @Schema(example = "true", defaultValue = "false", description = "Нужно ли оплачивать участие в событии")
    private boolean paid;
    @Schema(example = "10", defaultValue = "0",
            description = "Ограничение на количество участников. Значение 0 - означает отсутствие ограничения")
    @PositiveOrZero
    private int participantLimit;
    @Schema(example = "false", defaultValue = "true",
            description = "Нужна ли пре-модерация заявок на участие. Если true, " +
                    "то все заявки будут ожидать подтверждения инициатором события. " +
                    "Если false - то будут подтверждаться автоматически.")
    private boolean requestModeration = true;
}
