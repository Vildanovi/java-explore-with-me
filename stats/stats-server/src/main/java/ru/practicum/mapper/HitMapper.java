package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndPointHitDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HitMapper {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Hit mapEndPointHitDtoToHit(EndPointHitDto endPointHitDto) {
        return Hit.builder()
                .id(endPointHitDto.getId())
                .app(endPointHitDto.getApp())
                .uri(endPointHitDto.getUri())
                .ip(endPointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endPointHitDto.getTimestamp(), dateFormat))
                .build();
    }

    public EndPointHitDto mapHitToEndPointHitDto(Hit hit) {
        return EndPointHitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .format(hit.getTimestamp()))
                .build();
    }

}
