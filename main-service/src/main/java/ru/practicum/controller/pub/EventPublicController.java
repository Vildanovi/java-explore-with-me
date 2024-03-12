package ru.practicum.controller.pub;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constant.Constants;
import ru.practicum.mapper.EventMapper;
import ru.practicum.stats.dto.event.EventFullDto;
import ru.practicum.stats.dto.event.EventShortDto;
import ru.practicum.service.EventsService;
import ru.practicum.stats.dto.EndPointHitDto;
import ru.practicum.client.StatClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Public: События", description = "Публичный API для работы с событиями")
public class EventPublicController {

    private final EventsService eventsService;
    private final StatClient statClient;

    @Value("${service-name}")
    private String application;

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
                                               @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size,
                                               HttpServletRequest request) {
        createHit(request);
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
        createHit(request);
        return EventMapper.mapEventToEventFullDto(eventsService.getEventById(id));
    }

    private void createHit(HttpServletRequest request) {
        EndPointHitDto endpointHitRequestDto = EndPointHitDto.builder()
                .app(application)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        statClient.createHit(endpointHitRequestDto);
    }
}
