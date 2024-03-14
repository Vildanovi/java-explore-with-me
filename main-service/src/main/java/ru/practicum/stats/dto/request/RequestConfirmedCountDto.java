package ru.practicum.stats.dto.request;

import lombok.*;

@Getter
@Setter
public class RequestConfirmedCountDto {

    private Integer eventId;
    private Long count;

    public RequestConfirmedCountDto(Integer eventId, Long count) {
        this.eventId = eventId;
        this.count = count;
    }
}
