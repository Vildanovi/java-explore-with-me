package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.client.StatClient;
import ru.practicum.stats.dto.EndPointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatService {

    private final StatClient statClient;

    @Value("${service-name}")
    private String application;

    public void createHit(HttpServletRequest request) {
        log.info("Создаем Hit");
        EndPointHitDto endpointHitRequestDto = EndPointHitDto.builder()
                .app(application)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        statClient.createHit(endpointHitRequestDto);
    }
}
