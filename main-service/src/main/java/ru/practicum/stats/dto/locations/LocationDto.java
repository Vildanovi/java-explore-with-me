package ru.practicum.stats.dto.locations;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Широта и долгота места проведения события")
public class LocationDto {
    @Schema(example = "55.754167", description = "Широта")
    @Min(-90)
    @Max(90)
    private float lat;
    @Schema(example = "37.62", description = "Долгота")
    @Max(value = 180)
    private float lon;
}
