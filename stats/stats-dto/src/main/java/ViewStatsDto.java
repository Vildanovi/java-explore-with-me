import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {

    @Schema(description = "Название сервиса",
            example = "ewm-main-service")
    private String app;
    @Schema(description = "URI сервиса",
            example = "/events/1")
    private String uri;
    @Schema(description = "Количество просмотров",
            example = "6")
    private Integer hits;
}
