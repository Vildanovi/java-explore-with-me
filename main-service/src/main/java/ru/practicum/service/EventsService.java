package ru.practicum.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.constant.Constants;
import ru.practicum.exception.BadRequestException;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.model.enumerations.RequestStatus;
import ru.practicum.model.enumerations.SortParam;
import ru.practicum.pageable.OffsetBasedPageRequest;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.dto.locations.LocationDto;
import ru.practicum.stats.dto.event.*;
import ru.practicum.stats.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.stats.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.stats.dto.request.ParticipationRequestDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.ValidationBadRequestException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.enumerations.StateEvent;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
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
            throw new BadRequestException("Дата мероприятия ранее 2 часов: " + eventDate);
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
        UpdateEventAdminRequest.StateAction state;
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + eventId));
        if (eventDate != null) {
            if (isEventDateBefore2Ours(eventDate)) {
                throw new BadRequestException("Дата мероприятия ранее 2 часов: " + eventDate);
            } else {
                event.setEventDate(eventDate);
            }
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            try {
                state = UpdateEventAdminRequest.StateAction.valueOf(updateEventAdminRequest.getStateAction());
            } catch (IllegalArgumentException e) {
                throw new ValidationBadRequestException("Неизвестный параметр " + updateEventAdminRequest.getStateAction());
            }

            switch (state) {
                case REJECT_EVENT:
                    if (event.getState().equals(StateEvent.PUBLISHED)) {
                        throw new ValidationBadRequestException("Нельзя отменить опублиокванное событие");
                    }
                    event.setState(StateEvent.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    if (!event.getState().equals(StateEvent.PENDING)) {
                        throw new ValidationBadRequestException("Для публикация должен быть статус PENDING");
                    }
                    event.setState(StateEvent.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                default:
                    throw new ValidationBadRequestException("Неизвестный параметр состояния события");
            }
        }

        setUpdate(event, updateEventAdminRequest);
        return event;
    }

    //Private Controller
    @Transactional
    public Event updateEventByUser(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        LocalDateTime eventDate = updateEventUserRequest.getEventDate();
        UpdateEventUserRequest.StateAction state;
        Event event;
        event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Событие %d инициатора %d не найдено", eventId, userId)));

        if (eventDate != null) {
            if (isEventDateBefore2Ours(eventDate)) {
                throw new BadRequestException("Дата мероприятия ранее 2 часов: " + eventDate);
            } else {
                event.setEventDate(eventDate);
            }
        }

        if (event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ValidationBadRequestException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            try {
                state = UpdateEventUserRequest.StateAction.valueOf(updateEventUserRequest.getStateAction());
            } catch (IllegalArgumentException e) {
                throw new ValidationBadRequestException("Неизвестный параметр " + updateEventUserRequest.getStateAction());
            }
            switch (state) {
                case SEND_TO_REVIEW:
                    event.setState(StateEvent.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(StateEvent.CANCELED);
                    break;
                default:
                    throw new ValidationBadRequestException("Неизвестный параметр состояния события");
            }
        }

        setUpdate(event, updateEventUserRequest);
        return event;
    }

    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Integer userId,
                                                              Integer eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + eventId));
        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();
        List<ParticipationRequest> requestsData = participationRequestRepository.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());

        List<ParticipationRequestDto> participationRequests = participationRequestRepository
                .findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds())
                .stream()
                .map(ParticipationRequestMapper::mapParticipationRequestToParticipationRequestDto)
                .collect(Collectors.toList());

        for (ParticipationRequestDto request : participationRequests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ValidationBadRequestException("Cтатус можно изменить только у заявок, находящихся в состоянии ожидания");
            }
        }

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = EventRequestStatusUpdateResult
                .builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        int eventConfirmedRequests = requestsService.getEventConfirmedRequests(eventId);
        switch (eventRequestStatusUpdateRequest.getStatus()) {
            case CONFIRMED:
                if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
                    for (ParticipationRequest request : requestsData) {
                        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                            throw new ValidationBadRequestException("Нельзя отменить подтвержденную заявку");
                        }
                    }
                    requestsData.forEach(request -> request.setStatus(RequestStatus.CONFIRMED));
                    confirmedRequests.addAll(requestsData);
                } else if (event.getParticipantLimit() == eventConfirmedRequests) {
                    throw new ValidationBadRequestException(String
                            .format("Достигнут лимит %d по заявкам на событие %d", eventConfirmedRequests, eventId));
                } else {
                    for (ParticipationRequest request : requestsData) {
                        if (event.getParticipantLimit() > eventConfirmedRequests) {
                            request.setStatus(RequestStatus.CONFIRMED);
                            confirmedRequests.add(request);
                        } else {
                            request.setStatus(RequestStatus.REJECTED);
                            rejectedRequests.add(request);
                        }
                    }
                }
                break;
            case REJECTED:
                requestsData.forEach(request -> request.setStatus(RequestStatus.REJECTED));
                rejectedRequests.addAll(requestsData);
        }
        eventRequestStatusUpdateResult
                .setConfirmedRequests(confirmedRequests.stream()
                        .map(ParticipationRequestMapper::mapParticipationRequestToParticipationRequestDto)
                        .collect(Collectors.toList()));
        eventRequestStatusUpdateResult
                .setRejectedRequests(rejectedRequests.stream()
                        .map(ParticipationRequestMapper::mapParticipationRequestToParticipationRequestDto)
                        .collect(Collectors.toList()));
        return eventRequestStatusUpdateResult;
    }

    public List<Event> getAllEventsByUser(Integer userId, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size, Constants.SORT_DESC_ID);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        return events;
    }

    public Event getEventByUser(Integer userId, Integer eventId) {
        Event event;
        event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Событие %d инициатора %d не найдено", eventId, userId)));
        return event;
    }

    //AdminController
    public List<Event> getEvents(List<Integer> users, List<StateEvent> states, List<Integer> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<Event> events;
        Pageable pageable = new OffsetBasedPageRequest(from, size, Constants.SORT_DESC_EVENTDATE);
//        Pageable pageable = PageRequest.of(from / size, size, Constants.SORT_DESC_ID);
        BooleanBuilder searchParam = new BooleanBuilder();

        if (users != null) {
            BooleanExpression byUsersId = QEvent.event.initiator.id.in(users);
            searchParam.and(byUsersId);
        }
        if (states != null) {
            BooleanExpression byStates = QEvent.event.state.in(states);
            searchParam.and(byStates);
        }
        if (categories != null) {
            BooleanExpression byCategory = QEvent.event.category.id.in(categories);
            searchParam.and(byCategory);
        }
        BooleanExpression byEventDate = null;
        if (rangeStart == null && rangeEnd != null) {
            byEventDate = QEvent.event.eventDate.before(rangeEnd);
        }
        if (rangeStart != null && rangeEnd == null) {
            byEventDate = QEvent.event.eventDate.after(rangeStart);
        }
        if (rangeStart != null && rangeEnd != null) {
            byEventDate = QEvent.event.eventDate.between(rangeStart, rangeEnd);
        }

        if (rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAll(searchParam, pageable).getContent();
        } else {
            events = eventRepository.findAll(searchParam.and(byEventDate), pageable).getContent();
        }

        setHitsAndRequests(events);

        return events;
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

        if (text != null) {
            BooleanExpression byText = QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text));
            where.and(byText);
        }
        if (categories != null) {
            BooleanExpression byCategories = QEvent.event.category.id.in(categories);
            where.and(byCategories);
        }
        if (paid != null) {
            BooleanExpression byPaid = QEvent.event.paid.eq(paid);
            where.and(byPaid);
        }

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Дата начала не может быть раньше даты завершения");
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
        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getConfirmedRequest() != event.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        return events;
    }

    //PublicController
    public Event getEventById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + id));

        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new EntityNotFoundException("Не найдено событие со статусом опубликовано");
        }
        setHitsAndRequests(List.of(event));
        return event;
    }

    public List<ParticipationRequest> getRequestsByEventId(Integer userId, Integer eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + userId));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + eventId));
        return participationRequestRepository.findAllByEventId(eventId);
    }

    private Map<Integer, Integer> getHits(List<Integer> ids) {
        List<String> uris = ids.stream()
                .map(id -> String.format("/events/%d", id))
                .collect(Collectors.toList());
        List<ViewStatsDto> stats = statClient.getStats(LocalDateTime.now().minusYears(20), LocalDateTime.now(), uris, true);

        Map<Integer, Integer> hits = new HashMap<>();
        for (ViewStatsDto stat : stats) {
            Integer id = Integer.valueOf(stat.getUri().split("/", 0)[2]);
            hits.put(id, stat.getHits());
        }
        return hits;
    }

    private void setHitsAndRequests(List<Event> events) {
        List<Integer> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Integer, Integer> hits = getHits(ids);

        for (Event event : events) {
            event.setViews(hits.getOrDefault(event.getId(), 0));
        }

        Map<Integer, Integer> confirmedRequests = participationRequestRepository
                .findByEventAndStartAndEnd(ids, RequestStatus.CONFIRMED)
                .stream()
                .map(obj -> {
                            Integer id = obj.getEventId();
                            Integer count = Integer.parseInt(String.valueOf(obj.getCount()));
                            return new AbstractMap.SimpleEntry<>(id, count);
                        })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Event event : events) {
            event.setConfirmedRequest(confirmedRequests
                    .getOrDefault(event.getId(), 0));
        }
    }

    public boolean isEventDateBefore2Ours(LocalDateTime eventDate) {
        LocalDateTime validEventDate = LocalDateTime.now().plusHours(2);
        return eventDate.isBefore(validEventDate);
    }

    private Category getCategoryOrException(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + categoryId));
    }

    private void setUpdate(Event event, UpdateEventRequest updateEvent) {
        Integer categoryId = updateEvent.getCategory();
        if (categoryId != null) {
            event.setCategory(getCategoryOrException(categoryId));
        }

        String annotation = updateEvent.getAnnotation();
        String description = updateEvent.getDescription();
        LocationDto location = updateEvent.getLocation();
        Boolean paid = updateEvent.getPaid();
        Integer participantLimit = updateEvent.getParticipantLimit();
        Boolean requestModeration = updateEvent.getRequestModeration();
        String title = updateEvent.getTitle();

        if (annotation != null && !annotation.isBlank()) {
            event.setAnnotation(annotation);
        }
        if (description != null && !annotation.isBlank()) {
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
        if (title != null && !annotation.isBlank()) {
            event.setTitle(title);
        }
    }
}