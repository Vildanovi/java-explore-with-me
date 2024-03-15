package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.ValidationBadRequestException;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.Users;
import ru.practicum.model.enumerations.RequestStatus;
import ru.practicum.model.enumerations.StateEvent;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.repository.ParticipationRequestRepository;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestsService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<ParticipationRequest> getRequestsByUser(@PathVariable @Positive Integer userId) {
        return participationRequestRepository.findAllByRequesterId(userId);
    }

    @Transactional
    public ParticipationRequest createRequest(Integer userId, Integer eventId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект %d не найден", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект %d не найден", eventId)));

        List<ParticipationRequest> requests = participationRequestRepository.findAllByRequesterIdAndEventId(userId, eventId);
        if (!requests.isEmpty()) {
            throw new ValidationBadRequestException(String
                    .format("Заявка пользователя %d уже существует для события %d", userId, eventId));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ValidationBadRequestException("Инициатор события не может добавить запрос");
        }
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ValidationBadRequestException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() == getEventConfirmedRequests(eventId)) {
            throw new ValidationBadRequestException("Достигнут лимит запросов на участие");
        }

        ParticipationRequest participationRequest;
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest = ParticipationRequest.builder()
                    .event(event)
                    .requester(user)
                    .created(LocalDateTime.now())
                    .status(RequestStatus.CONFIRMED)
                    .build();
        } else {
            participationRequest = ParticipationRequest.builder()
                    .event(event)
                    .requester(user)
                    .created(LocalDateTime.now())
                    .status(RequestStatus.PENDING)
                    .build();
        }

        return participationRequestRepository.save(participationRequest);
    }

    @Transactional
    public ParticipationRequest cancelRequest(Integer userId, Integer requestId) {
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + requestId));

        if (!participationRequest.getRequester().getId().equals(userId)) {
            throw new ValidationBadRequestException(String
                    .format("Для запроса %d пользователь %d не является владельцем", requestId, userId));
        }
        participationRequest.setStatus(RequestStatus.CANCELED);
        return participationRequest;
    }

    protected int getEventConfirmedRequests(Integer eventId) {
        return participationRequestRepository.countAllByEvent_IdAndStatusIs(eventId,
                RequestStatus.CONFIRMED);
    }
}
