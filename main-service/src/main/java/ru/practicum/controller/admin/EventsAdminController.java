package ru.practicum.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constant.Constants;
import ru.practicum.stats.dto.event.EventFullDto;
import ru.practicum.stats.dto.event.UpdateEventAdminRequest;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.enumerations.StateEvent;
import ru.practicum.service.EventsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin: События", description = "API для работы с событиями")
public class EventsAdminController {

    private final EventsService eventsService;

    @GetMapping
    @Operation(
            summary = "Поиск событий",
            description = "Эндпоинт возвращает полную информацию обо всех событиях подходящих " +
                    "под переданные условия"
    )
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<StateEvent> states,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeEnd,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return eventsService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size)
                .stream()
                .map(EventMapper::mapEventToEventFullDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}")
    @Operation(
            summary = "Редактирование данных события и его статуса (отклонение/публикация).",
            description = "Редактирование данных любого события администратором. Валидация данных не требуется.\n" +
                    "Обратите внимание:\n" +
                    "- дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)\n" +
                    "- событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)\n" +
                    "- событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)"
    )
    public EventFullDto updateEventById(@PathVariable @Positive Integer eventId,
                                        @RequestBody @Valid UpdateEventAdminRequest updateEventDto) {
        return EventMapper.mapEventToEventFullDto(eventsService.updateEventById(eventId, updateEventDto));
    }
}
