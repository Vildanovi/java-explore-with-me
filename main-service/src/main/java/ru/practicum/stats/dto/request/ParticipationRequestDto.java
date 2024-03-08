package ru.practicum.stats.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.constant.Constants;
import ru.practicum.model.enumerations.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Заявка на участие в событии")
public class ParticipationRequestDto {
    @Schema(example = "3", description = "Идентификатор заявки")
    private int id;
    @Schema(example = "1",
            description = "Идентификатор события")
    private int event;
    @Schema(example = "2",
            description = "Идентификатор пользователя, отправившего заявку")
    private int requester;
    @Schema(example = "2022-09-06T21:10:05.432",
            description = "Дата и время создания заявки")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    private LocalDateTime created;
    @Schema(example = "PENDING", description = "Статус заявки")
    private RequestStatus status;
}
