package ru.practicum.stats.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.constant.Constants;
import ru.practicum.stats.dto.Locations.LocationDto;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для изменения информации о событии. Если поле в запросе не указано (равно null) " +
        "- значит изменение этих данных не треубется.")
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "annotation < 20 или > 2000 символов")
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000, message = "description < 20 или > 7000 символов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Size(min = 3, max = 120, message = "title < 20 или > 120 символов")
    private String title;
}
