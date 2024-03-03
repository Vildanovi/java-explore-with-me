package ru.practicum.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Результат подтверждения/отклонения заявок на участие в событии")
public class EventRequestStatusUpdateResult {
    @NotNull
    private List<ParticipationRequestDto> confirmedRequests;
    @NotNull
    private List<ParticipationRequestDto> rejectedRequests;
}
