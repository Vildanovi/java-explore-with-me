package ru.practicum.controller.priv;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.stats.dto.event.EventFullDto;
import ru.practicum.stats.dto.event.EventShortDto;
import ru.practicum.stats.dto.event.NewEventDto;
import ru.practicum.stats.dto.event.UpdateEventUserRequest;
import ru.practicum.stats.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.stats.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.stats.dto.request.ParticipationRequestDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.EventsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/events", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Private: События", description = "Закрытый API для работы с событиями")
public class EventPrivateController {

    private final EventsService eventsService;

    @GetMapping
    @Operation(
            summary = "Получение событий, добавленных текущим пользователем",
            description = "В случае, если по заданным фильтрам не найдено ни одного события, возвращает " +
                    "пустой список"
    )
    public List<EventShortDto> getAllEventsByUser(@PathVariable @Positive Integer userId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventsService.getAllEventsByUser(userId, from, size)
                .stream()
                .map(EventMapper::mapEventToEventShortDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Добавление нового события",
            description = "Обратите внимание: дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента"
    )
    public EventFullDto createEvent(@PathVariable @Positive Integer userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        return EventMapper.mapEventToEventFullDto(eventsService.createEvent(userId, newEventDto));
    }

    @GetMapping("/{eventId}")
    @Operation(
            summary = "Получение полной информации о событии добавленном текущим пользователем",
            description = "В случае, если события с заданным id не найдено, возвращает статус код 404"
    )
    public EventFullDto getEventByUser(@PathVariable @Positive Integer userId,
                                       @PathVariable @Positive Integer eventId) {
        return EventMapper
                .mapEventToEventFullDto(eventsService
                        .getEventByUser(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    @Operation(
            summary = "Изменение события добавленного текущим пользователем",
            description = "Обратите внимание: \n" +
                    "- изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)\n" +
                    "- дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)"
    )
    public EventFullDto updateEventByUser(@PathVariable @Positive Integer userId,
                                          @PathVariable @Positive Integer eventId,
                                          @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return EventMapper.mapEventToEventFullDto(eventsService
                .updateEventByUser(userId, eventId, updateEventUserRequest));
    }

    @GetMapping("/{eventId}/requests")
    @Operation(
            summary = "Получение информации о запросах на участие в событии текущего пользователя",
            description = "В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список"
    )
    public List<ParticipationRequestDto> getRequestsByEventId(@PathVariable @Positive Integer userId,
                                                              @PathVariable @Positive Integer eventId) {
        return eventsService.getRequestsByEventId(userId, eventId)
                .stream()
                .map(ParticipationRequestMapper::mapParticipationRequestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}/requests")
    @Operation(
            summary = "Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя",
            description = "Обратите внимание:\n" +
                    "- если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется\n" +
                    "- нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)\n" +
                    "- статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)\n" +
                    "- если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить"
    )
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable @Positive Integer userId,
                                                              @PathVariable @Positive Integer eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return eventsService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
