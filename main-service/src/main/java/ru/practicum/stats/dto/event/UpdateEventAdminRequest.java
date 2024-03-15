package ru.practicum.stats.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для изменения информации о событии. Если поле в запросе не указано (равно null) " +
        "- значит изменение этих данных не треубется.")
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private String stateAction;

    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT,
    }
}
