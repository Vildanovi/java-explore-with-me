package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.EndPointHitDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.service.HitService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final HitService hitService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndPointHitDto createHit(@RequestBody @Valid EndPointHitDto endPointHitDto) {
        log.debug("Сохраняем параметры запроса к эндпоинту {}", endPointHitDto);
        return HitMapper.mapHitToEndPointHitDto(hitService.createHit(endPointHitDto));
    }

}
