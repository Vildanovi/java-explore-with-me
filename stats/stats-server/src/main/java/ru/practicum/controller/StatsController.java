package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndPointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.service.HitService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {

    private final HitService hitService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/hit")
    @Operation(summary = "Сохранение информации о том, что к эндпоинту был запрос")
    public EndPointHitDto createHit(@RequestBody @Valid EndPointHitDto endPointHitDto) {
        log.debug("Сохраняем параметры запроса к эндпоинту {}", endPointHitDto);
        return HitMapper.mapHitToEndPointHitDto(hitService.createHit(endPointHitDto));
    }

    @GetMapping("/stats")
    @Operation(summary = "Получение статистики по посещениям.")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start")  String start,
                                       @RequestParam(name = "end")  String end,
                                       @RequestParam(name = "uris", required = false) List<String> uris,
                                       @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.debug("Получаем статистику для uris: {} start: {} end: {} unique: {}", uris, start, end, unique);
        return hitService.getStats(start, end, uris, unique)
                .stream()
                .map(HitMapper::mapStatsToViewStatsDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<EndpointHit> getHits() {
        return hitService.getHits();
    }
}
