package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.stats.dto.Locations.LocationDto;
import ru.practicum.stats.dto.event.*;
import ru.practicum.stats.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.stats.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.stats.dto.request.ParticipationRequestDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.ValidationBadRequestException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Users;
import ru.practicum.model.enumerations.StateAction;
import ru.practicum.model.enumerations.StateEvent;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatClient statClient;

    @Transactional
    public Event createEvent(int userId, NewEventDto newEventDto) {
        LocalDateTime eventDate = newEventDto.getEventDate();
        if (isEventDateBefore2Ours(eventDate)) {
            throw new ValidationBadRequestException("Дата мероприятия ранее 2 часов: " + eventDate);
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + userId));

        int categoryId = newEventDto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + categoryId));

        Event event = EventMapper.mapNewEventDtoToEvent(newEventDto);
        event.setEventDate(eventDate);
        event.setInitiator(user);
        event.setCategory(category);
        event.setState(StateEvent.PENDING);

        return eventRepository.save(event);
    }

    //EventsAdminController
    @Transactional
    public Event updateEventById(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        LocalDateTime eventDate = updateEventAdminRequest.getEventDate();

        if (eventDate != null) {
            if (isEventDateBefore2Ours(eventDate)) {
                throw new ValidationBadRequestException("Дата мероприятия ранее 2 часов: " + eventDate);
            }
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + eventId));

        String stateAction = updateEventAdminRequest.getStateAction();
        if (stateAction != null) {
            if (!StateAction.PUBLISH_EVENT.toString().equals(stateAction) && !StateAction.REJECT_EVENT.toString().equals(stateAction)) {
                throw new ValidationBadRequestException("Статус не существует: " + stateAction);
            }

            StateAction state = StateAction.valueOf(stateAction);

            if (state.equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(StateEvent.PENDING);
            } else if (state.equals(StateAction.PUBLISH_EVENT)) {
                event.setState(StateEvent.PUBLISHED);
            } else {
                event.setState(StateEvent.CANCELED);
            }

            if (!event.getState().equals(StateEvent.PENDING) && state.equals(StateAction.PUBLISH_EVENT)) {
                throw new ValidationBadRequestException("Событие с пользовательским статусом: PENDING не может иметь статус адмнистратора: PUBLISH_EVENT");
            }
            if (event.getState().equals(StateEvent.PUBLISHED) && state.equals(StateAction.REJECT_EVENT)) {
                throw new ValidationBadRequestException("Событие с пользовательским статусом: PUBLISH не может иметь статус адмнистратора: PUBLISH_EVENT");
            }
        }

        if (updateEventAdminRequest.getCategory() != null) {
            int idCat = updateEventAdminRequest.getCategory();
            Category category = categoryRepository.findById(idCat)
                    .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + idCat));
            event.setCategory(category);
        }

        String annotation = updateEventAdminRequest.getAnnotation();
        String description = updateEventAdminRequest.getDescription();
        LocationDto location = updateEventAdminRequest.getLocation();
        Boolean paid = updateEventAdminRequest.getPaid();
        Integer participantLimit = updateEventAdminRequest.getParticipantLimit();
        Boolean requestModeration = updateEventAdminRequest.getRequestModeration();
        String title = updateEventAdminRequest.getTitle();
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (description != null) {
            event.setDescription(description);
        }
        if (location != null) {
            event.setLon(location.getLon());
            event.setLat(location.getLat());
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (title != null) {
            event.setTitle(title);
        }
//        addConfirmedRequests(List.of(event));
        return event;
    }

    @Transactional
    public EventFullDto updateEventByUser(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        return null;
    }

    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Integer userId,
                                                              Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return null;
    }

    public List<EventShortDto> getAllEventsByUser(Integer userId, int from, int size) {
        return Collections.emptyList();
    }

    public EventFullDto getEventByUser(Integer userId, Integer eventId) {
        return null;
    }

    //AdminController
    public List<EventFullDto> getEvents(List<Integer> users, List<StateEvent> states, List<Integer> categories,
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

    public List<ParticipationRequestDto> getRequestsByEventId(Integer userId, Integer eventId) {
        return null;
    }

    public EventFullDto findPublishedById(Integer id) {
        return null;
    }

    public boolean isEventDateBefore2Ours(LocalDateTime eventDate) {
        LocalDateTime validEventDate = LocalDateTime.now().plusHours(2);
        return eventDate.isBefore(validEventDate);
    }

//    private Map<Integer, Integer> getEventViews(Collection<Event> events) {
//        Map<String, Integer> eventUrisAndId = events.stream()
//                .map(Event::getId)
//                .collect(Collectors.toMap(id -> "/events/" + id, Function.identity()));
//        List<ViewStatsDto> statsDto = statClient.getStats(
//                ViewStatsDto.builder()
//                        .uri(eventUrisAndId.keySet())
//                        .unique(true)
//                        .build()
//        );
//        return statsDto.stream()
//                .collect(Collectors.toMap(
//                        stat -> eventUrisAndId.get(stat.getUri()), ViewStatsDto::getHits
//                ));
//    }

}
