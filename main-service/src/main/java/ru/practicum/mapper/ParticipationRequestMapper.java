package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.stats.dto.request.ParticipationRequestDto;

@UtilityClass
public class ParticipationRequestMapper {

    public ParticipationRequestDto mapParticipationRequestToParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated())
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }
}
