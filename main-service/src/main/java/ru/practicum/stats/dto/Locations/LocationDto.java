package ru.practicum.stats.dto.Locations;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Широта и долгота места проведения события")
public class LocationDto {
    @Schema(example = "55.754167", description = "Широта")
    private float lat;
    @Schema(example = "37.62", description = "Долгота")
    private float lon;
}
