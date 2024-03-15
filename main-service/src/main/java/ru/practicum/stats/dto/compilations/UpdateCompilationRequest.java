package ru.practicum.stats.dto.compilations;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@Schema(example = "Изменение информации о подборке событий. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.")
public class UpdateCompilationRequest {
    @Schema(description = "Список id событий подборки для полной замены текущего списка")
    private Set<Integer> events;
    @Schema(example = "true", description = "Закреплена ли подборка на главной странице сайта")
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "title < 1 или > 50 симвлов")
    @Schema(example = "Необычные фотозоны", description = "Заголовок подборки")
    private String title;


}
