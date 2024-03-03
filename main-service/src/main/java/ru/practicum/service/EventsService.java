package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.enumerations.EventState;
import ru.practicum.repository.EventsRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventsRepository eventsRepository;

    public List<EventShortDto> getAllEventsByUser(Integer userId, int from, int size) {
        return Collections.emptyList();
    }

    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        return null;
    }

    public EventFullDto getEventByUser(Integer userId, Integer eventId) {
        return null;
    }

    //AdminController
    public List<EventFullDto> getEvents(List<Integer> users, List<EventState> states, List<Integer> categories,
                                        LocalDateTime start, LocalDateTime end, int from, int size) {
        return null;
    }

    //PublicController
    public List<EventShortDto> getEventsPublic(String text, List<Integer> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               boolean onlyAvailable, String sort, int from, int size) {
        return null;
    }

    public EventFullDto getEventById(Integer id) {
        return null;
    }


    //EventsAdminController
    public EventFullDto updateEventById(int eventId, UpdateEventUserRequest updateEventDto) {
        return null;
    }

    public EventFullDto updateEventByUser(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        return null;
    }

    public List<ParticipationRequestDto> getRequestsByEventId(Integer userId, Integer eventId) {
        return null;
    }

    public EventRequestStatusUpdateResult updateRequestStatus(Integer userId,
                                                              Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return null;
    }
}
