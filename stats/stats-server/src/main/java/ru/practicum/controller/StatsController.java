package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.EndPointHitDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.service.HitService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final HitService hitService;

    @PostMapping("/hit")
    public EndPointHitDto createHit(@RequestBody EndPointHitDto endPointHitDto) {
        Hit hit = hitService.createHit(endPointHitDto);
        return HitMapper.mapHitToEndPointHitDto(hit);
    }

}
