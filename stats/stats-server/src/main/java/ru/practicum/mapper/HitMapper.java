package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndPointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

@UtilityClass
public class HitMapper {

    public EndpointHit mapEndPointHitDtoToHit(EndPointHitDto endPointHitDto) {
        return EndpointHit.builder()
                .id(endPointHitDto.getId())
                .app(endPointHitDto.getApp())
                .uri(endPointHitDto.getUri())
                .ip(endPointHitDto.getIp())
                .timestamp(endPointHitDto.getTimestamp())
                .build();
    }

    public EndPointHitDto mapHitToEndPointHitDto(EndpointHit endpointHit) {
        return EndPointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
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
