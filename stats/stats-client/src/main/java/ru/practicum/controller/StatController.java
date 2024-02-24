package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.EndPointHitDto;
import ru.practicum.client.StatClient;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatClient statClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> postItem(@RequestBody @Valid EndPointHitDto endPointHitDto) {
        log.debug("Сохраняем параметры запроса к эндпоинту {}", endPointHitDto);
        return statClient.createHit(endPointHitDto);
    }
}
