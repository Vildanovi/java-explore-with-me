package ru.practicum.dto.compilations;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@Schema(example = "Подборка событий")
public class NewCompilationsDto {
    @Schema(example = "List [ 1, 2, 3 ]",
            description = "Список идентификаторов событий входящих в подборку")
    private Set<Integer> events;
    @Schema(example = "false", defaultValue = "false",
            description = "Закреплена ли подборка на главной странице сайта")
    private boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50, message = "title < 1 или > 50 симвлов")
    @Schema(example = "Летние концерты", description = "Заголовок подборки")
    private String title;
}