package ru.practicum.stats.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.constant.Constants;
import ru.practicum.stats.dto.locations.LocationDto;
import ru.practicum.stats.dto.category.CategoryDto;
import ru.practicum.stats.dto.user.UserShortDto;
import ru.practicum.model.enumerations.StateEvent;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    @Schema(example = "1", description = "Идентификатор")
    private int id;
    @Schema(example = "Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории",
            description = "Краткое описание")
    private String annotation;
    private CategoryDto category;
    @Schema(example = "5", description = "Количество одобренных заявок на участие в данном событии")
    private int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    private LocalDateTime createdOn;
    @Schema(example = "Что получится, если соединить кукурузу и полёт? Создатели " +
            "\"Шоу летающей кукурузы\" испытали эту идею на практике и воплотили " +
            "в жизнь инновационный проект, предлагающий свежий взгляд на развлечения...",
            description = "Полное описание события")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    @Schema(example = "2024-12-31 15:10:05",
            description = "Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\")")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    @Schema(example = "true", defaultValue = "false", description = "Нужно ли оплачивать участие в событии")
    private boolean paid;
    @Schema(example = "10", defaultValue = "0", description = "Ограничение на количество участников. " +
            "Значение 0 - означает отсутствие ограничения")
    private int participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    private LocalDateTime publishedOn;
    @Schema(example = "false", defaultValue = " true",
            description = "Нужна ли пре-модерация заявок на участие. Если true, " +
                    "то все заявки будут ожидать подтверждения инициатором события. " +
                    "Если false - то будут подтверждаться автоматически.")
    private boolean requestModeration;
    @Schema(example = "PUBLISHED", description = "Список состояний жизненного цикла события")
    private StateEvent state;
    @Schema(example = "Знаменитое шоу 'Летающая кукуруза'", description = "Заголовок")
    private String title;
    @Schema(example = "999", description = "Количество просмотров события")
    private int views;
}
