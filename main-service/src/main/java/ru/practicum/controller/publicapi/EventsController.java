package ru.practicum.controller.publicapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.service.EventsService;
import ru.practicum.dto.EndPointHitDto;
import ru.practicum.client.StatClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/events")
@Tag(name = "Public: События", description = "Публичный API для работы с событиями")
public class EventsController {

    private final EventsService eventsService;
    private final StatClient statClient;

    @Value("${service-name}")
    private String serviceName;

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
    public List<EventShortDto> getEventsPublic(@RequestParam(defaultValue = "") String text,
                                               @RequestParam(defaultValue = "") List<Integer> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) LocalDateTime rangeStart,
                                               @RequestParam(required = false) LocalDateTime rangeEnd,
                                               @RequestParam(required = false) boolean onlyAvailable,
                                               @RequestParam(defaultValue = "") String sort,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size,
                                               HttpServletRequest request) {
        createHit(request);
        return eventsService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
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
        return eventsService.getEventById(id);
    }

    private void createHit(HttpServletRequest request) {
        EndPointHitDto endpointHitRequestDto = EndPointHitDto.builder()
                .app(serviceName)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        statClient.createHit(endpointHitRequestDto);
    }

}
