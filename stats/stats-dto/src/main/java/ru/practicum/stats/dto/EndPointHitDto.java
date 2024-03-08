package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndPointHitDto {
    @Schema(description = "Идентификатор записи",
            example = "1")
    private int id;
    @Schema(description = "Идентификатор сервиса для которого записывается информация",
            example = "ewm-main-service")
    @Size(max = 255, message = "Имя > 255 символов")
    @NotBlank(message = "app не может быть null")
    private String app;
    @Schema(description = "URI для которого был осуществлен запрос",
            example = "/events/1")
    @Size(max = 255, message = "Имя > 255 символов")
    @NotBlank(message = "uri не может быть null")
    private String uri;
    @Schema(description = "IP-адрес пользователя, осуществившего запрос",
            example = "192.163.0.1")
    @Size(max = 255, message = "Имя > 255 символов")
    @NotBlank(message = "ip не может быть null")
    private String ip;
    @Schema(description = "Дата и время, когда был совершен запрос к эндпоинту " +
            "(в формате \"yyyy-MM-dd HH:mm:ss\")",
            example = "2022-09-06 11:00:23")
    @NotNull(message = "timestamp не может быть null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}
