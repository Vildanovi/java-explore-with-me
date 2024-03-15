package ru.practicum.stats.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(example = "Данные для добавления новой категории")
public class NewCategoryDto {
    @Schema(example = "Концерты", description = "Название категории")
    @Size(max = 50, message = "name < 1 или > 50 символов")
    @NotBlank
    private String name;
}
