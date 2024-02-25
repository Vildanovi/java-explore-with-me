package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndPointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.constant.Constants;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;

@UtilityClass
public class HitMapper {

    public EndpointHit mapEndPointHitDtoToHit(EndPointHitDto endPointHitDto) {
        return EndpointHit.builder()
                .id(endPointHitDto.getId())
                .app(endPointHitDto.getApp())
                .uri(endPointHitDto.getUri())
                .ip(endPointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endPointHitDto.getTimestamp(), Constants.DATE_PATTERN))
                .build();
    }

    public EndPointHitDto mapHitToEndPointHitDto(EndpointHit endpointHit) {
        return EndPointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(Constants.DATE_PATTERN
                        .format(endpointHit.getTimestamp()))
                .build();
    }

    public ViewStatsDto mapStatsToViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(Math.toIntExact(viewStats.getHits()))
                .build();
    }
}
