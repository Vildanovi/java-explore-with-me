package ru.practicum.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(example = "Категория")
public class CategoryDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "Идентификатор категории")
    private int id;
    @Schema(example = "Концерты",
            description = "Название категории")
    private String name;
}
