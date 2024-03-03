package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.repository.RequestsRepository;

import javax.validation.constraints.Positive;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestsService {

    private final RequestsRepository requestsRepository;

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
