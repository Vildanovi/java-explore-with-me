package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.constant.Constants;
import ru.practicum.dto.Locations.LocationDto;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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
