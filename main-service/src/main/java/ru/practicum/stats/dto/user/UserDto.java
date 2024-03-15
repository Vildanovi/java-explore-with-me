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
public class UserDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "Идентификатор")
    private Integer id;
    private String name;
    private String email;
}
