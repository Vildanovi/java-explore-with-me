package ru.practicum.controller.adminapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.model.enumerations.EventState;
import ru.practicum.service.EventsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin")
@Tag(name = "Admin: События", description = "API для работы с событиями")
public class EventsAdminController {

    private final EventsService eventsService;

    @GetMapping("/events")
    @Operation(
            summary = "Поиск событий",
            description = "Эндпоинт возвращает полную информацию обо всех событиях подходящих " +
                    "под переданные условия"
    )
    public List<EventFullDto> getEvents(@RequestParam(defaultValue = "") List<Integer> users,
                                        @RequestParam(defaultValue = "") List<EventState> states,
                                        @RequestParam(defaultValue = "") List<Integer> categories,
                                        @RequestParam(required = false) LocalDateTime start,
                                        @RequestParam(required = false) LocalDateTime end,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(defaultValue = "10") @Positive int size) {
        return eventsService.getEvents(users, states, categories, start, end, from, size);
    }

    @PatchMapping("/events/{eventId}")
    @Operation(
            summary = "Редактирование данных события и его статуса (отклонение/публикация).",
            description = "Редактирование данных любого события администратором. Валидация данных не требуется.\n" +
                    "Обратите внимание:\n" +
                    "- дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)\n" +
                    "- событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)\n" +
                    "- событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)"
    )
    public EventFullDto updateEventById(@PathVariable @Positive Integer eventId,
                                        @RequestBody @Valid UpdateEventUserRequest updateEventDto) {
        return eventsService.updateEventById(eventId, updateEventDto);
    }
}
