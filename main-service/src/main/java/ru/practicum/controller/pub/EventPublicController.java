package ru.practicum.controller.pub;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constant.Constants;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.StatService;
import ru.practicum.stats.dto.event.EventFullDto;
import ru.practicum.stats.dto.event.EventShortDto;
import ru.practicum.service.EventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Public: События", description = "Публичный API для работы с событиями")
public class EventPublicController {

    private final EventsService eventsService;
    private final StatService statService;

    @GetMapping
    @Operation(
            summary = "Получение событий с возможностью фильтрации",
            description = "Обратите внимание:\n" +
                    "- это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события\n" +
                    "- текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв\n" +
                    "- если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, " +
                    "которые произойдут позже текущей даты и времени\n" +
                    "- информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие\n" +
                    "- информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики\n" +
                    "В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список"
    )
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN) LocalDateTime rangeEnd,
                                               @RequestParam(required = false) boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size,
                                               HttpServletRequest request) {
        statService.createHit(request);
        return eventsService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size)
                .stream()
                .map(EventMapper::mapEventToEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение подробной информации об опубликованном событии по его идентификатору",
            description = "Обратите внимание:\n" +
                    "- событие должно быть опубликовано\n" +
                    "- информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов\n" +
                    "- информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики\n" +
                    "- В случае, если события с заданным id не найдено, возвращает статус код 404"
    )
    public EventFullDto getEventById(@PathVariable @Positive Integer id,
                                     HttpServletRequest request) {
        statService.createHit(request);
        return EventMapper.mapEventToEventFullDto(eventsService.getEventById(id));
    }
}
