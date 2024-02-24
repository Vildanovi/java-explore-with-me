import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {

    @Schema(description = "Идентификатор записи",
            example = "1")
    private int id;
    @Schema(description = "Идентификатор сервиса для которого записывается информация",
            example = "ewm-main-service")
    private String app;
    @Schema(description = "URI для которого был осуществлен запрос",
            example = "/events/1")
    private String uri;
    @Schema(description = "IP-адрес пользователя, осуществившего запрос",
            example = "192.163.0.1")
    private String ip;
    @Schema(description = "Дата и время, когда был совершен запрос к эндпоинту " +
            "(в формате \"yyyy-MM-dd HH:mm:ss\")",
            example = "2022-09-06 11:00:23")
    private String timestamp;

}
