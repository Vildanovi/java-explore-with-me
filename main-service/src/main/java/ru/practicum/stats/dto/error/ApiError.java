package ru.practicum.stats.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сведения об ошибке")
public class ApiError {
    @Schema(example = "List []",
            description = "Список стектрейсов или описания ошибок")
    private String errors;
    @Schema(example = "FORBIDDEN",
            description = "Код статуса HTTP-ответа")
    private String status;
    @Schema(example = "For the requested operation the conditions are not met.",
            description = "Общее описание причины ошибки")
    private String reason;
    @Schema(example = "Only pending or canceled events can be changed",
            description = "Сообщение об ошибке")
    private String message;
    @Schema(example = "2022-06-09 06:27:23",
            description = "Дата и время когда произошла ошибка (в формате \"yyyy-MM-dd HH:mm:ss\")")
    private String timestamp;
}
