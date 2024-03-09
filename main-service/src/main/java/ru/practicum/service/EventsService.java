package ru.practicum.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.exception.BadRequestException;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.model.enumerations.RequestStatus;
import ru.practicum.model.enumerations.SortParam;
import ru.practicum.pageable.OffsetBasedPageRequest;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.stats.dto.Locations.LocationDto;
import ru.practicum.stats.dto.event.*;
import ru.practicum.stats.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.stats.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.stats.dto.request.ParticipationRequestDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.ValidationBadRequestException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.enumerations.StateAction;
import ru.practicum.model.enumerations.StateEvent;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.constant.Constants.SORT_ASC_EVENT_DATE;
import static ru.practicum.constant.Constants.SORT_DESC_VIEWS;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final RequestsService requestsService;
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
        event.setInitiator(user);
        event.setCategory(category);
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

//            if (!event.getState().equals(StateEvent.PENDING) && state.equals(StateAction.PUBLISH_EVENT)) {
//                throw new ValidationBadRequestException("Событие с пользовательским статусом: PENDING не может иметь статус адмнистратора: PUBLISH_EVENT");
//            }
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
                                                              Integer eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + eventId));
        List<ParticipationRequestDto> participationRequests = participationRequestRepository
                .findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds())
                .stream()
                .map(ParticipationRequestMapper::mapParticipationRequestToParticipationRequestDto)
                .collect(Collectors.toList());
        participationRequests
                .stream()
                .filter(request -> request.getStatus().equals(RequestStatus.PENDING))
                .forEach(request -> {
                    throw new ValidationBadRequestException("Cтатус можно изменить только у заявок, находящихся в состоянии ожидания");
                });

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = EventRequestStatusUpdateResult
                .builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            eventRequestStatusUpdateResult
                    .getConfirmedRequests()
                    .addAll(participationRequests);
        }

        int eventConfirmedRequests = requestsService.getEventConfirmedRequests(eventId);
        if (event.getParticipantLimit() == eventConfirmedRequests) {
            throw new ValidationBadRequestException(String
                    .format("Достигнут лимит %d по заявкам на событие %d", eventConfirmedRequests, eventId));
        }

        if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
            participationRequests.forEach(requestDto -> requestDto.setStatus(RequestStatus.REJECTED));
            eventRequestStatusUpdateResult
                    .getRejectedRequests()
                    .addAll(participationRequests);
        }


        int reserve = event.getParticipantLimit() - eventConfirmedRequests;

        for (ParticipationRequestDto requestDto : participationRequests) {
            if (reserve > 0) {
                requestDto.setStatus(RequestStatus.CONFIRMED);
                eventRequestStatusUpdateResult.getConfirmedRequests().add(requestDto);
                --reserve;
            } else {
                requestDto.setStatus(RequestStatus.REJECTED);
                eventRequestStatusUpdateResult.getRejectedRequests().add(requestDto);
            }
        }
        return eventRequestStatusUpdateResult;
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
    public List<Event> getEventsPublic(String text, List<Integer> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               boolean onlyAvailable, String sort, int from, int size) {
        List<Event> events;
        SortParam sortParam = SortParam.valueOf(sort);
        Pageable pageable;
        switch (sortParam) {
            case EVENT_DATE:
                pageable = new OffsetBasedPageRequest(from, size, SORT_ASC_EVENT_DATE);
                break;
            case VIEWS:
                pageable = new OffsetBasedPageRequest(from, size, SORT_DESC_VIEWS);
                break;
            default:
                throw new ValidationBadRequestException("Недопустимый параметр сортировки: " + sortParam);
        }

        BooleanBuilder where = new BooleanBuilder();

        BooleanExpression byPublishState = QEvent.event.state.eq(StateEvent.PUBLISHED);
        where.and(byPublishState);

        if (!text.isBlank()) {
            BooleanExpression byText = QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text));
            where.and(byText);
        }
        if (!categories.isEmpty()) {
            BooleanExpression byCategories = QEvent.event.category.id.in(categories);
            where.and(byCategories);
        }
        if (paid != null) {
            BooleanExpression byPaid = QEvent.event.paid.eq(paid);
            where.and(byPaid);
        }

        BooleanExpression byEventDate;
        if (rangeStart == null && rangeEnd == null) {
            LocalDateTime now = LocalDateTime.now();
            byEventDate = QEvent.event.eventDate.after(now);
        } else if (rangeStart == null) {
            byEventDate = QEvent.event.eventDate.before(rangeEnd);
        } else if (rangeEnd == null) {
            byEventDate = QEvent.event.eventDate.after(rangeStart);
        } else {
            byEventDate = QEvent.event.eventDate.between(rangeStart, rangeEnd);
        }
        where.and(byEventDate);

        events = eventRepository.findAll(where, pageable).getContent();
//        addConfirmedRequests(events);

        if (onlyAvailable) {
            events = events.stream().filter(ev -> ev.getConfirmedRequest() != ev.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        return events;
    }

    //PublicController
    public Event getEventById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + id));

        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new BadRequestException("Событие не опубликовано");
        }
//        addConfirmedRequests(List.of(event));
//        addViews();

        return event;
    }

    public List<ParticipationRequest> getRequestsByEventId(Integer userId, Integer eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + userId));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + eventId));
        return participationRequestRepository.findAllByEventId(eventId);
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
