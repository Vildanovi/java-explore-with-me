package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.stats.dto.request.ParticipationRequestDto;
import ru.practicum.repository.ParticipationRequestRepository;

import javax.validation.constraints.Positive;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestsService {

    private final ParticipationRequestRepository participationRequestRepository;

    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable @Positive Integer userId) {
        return null;
    }

    public ParticipationRequestDto createRequest(Integer userId, Integer eventId) {
        return null;
    }

    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        return null;
    }

}
