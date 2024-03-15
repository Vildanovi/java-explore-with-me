package ru.practicum.stats.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Пользователь (краткая информация)")
public class UserShortDto {
    @Schema(example = "1", description = "Идентификатор")
    private Integer id;
    @Schema(example = "Фёдоров Матвей", description = "Имя")
    private String name;
}
