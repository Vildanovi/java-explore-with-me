package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.BadRequestException;
import ru.practicum.stats.dto.EndPointHitDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitService {

    private final HitRepository hitRepository;

    @Transactional
    public EndpointHit createHit(EndPointHitDto endPointHitDto) {
        return hitRepository.save(HitMapper.mapEndPointHitDtoToHit(endPointHitDto));
    }

    public List<ViewStats> getStats(LocalDateTime startDate, LocalDateTime endDate, List<String> uris, Boolean isUnique) {
        List<ViewStats> stats;
//        if (startDate.equals(endDate) || endDate.isBefore(startDate))
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("Некорректные даты");
        }
        if (uris != null) {
            if (!isUnique) {
                stats = hitRepository.findByUrisAndStartAndEnd(startDate, endDate, uris);
            } else {
                stats = hitRepository.findUniqueByUrisAndStartAndEnd(startDate, endDate, uris);
            }
        } else {
            if (!isUnique) {
                stats = hitRepository.findByStartAndEnd(startDate, endDate);
            } else {
                stats = hitRepository.findUniqueByStartAndEnd(startDate, endDate);
            }
        }
        return stats;
    }
}
