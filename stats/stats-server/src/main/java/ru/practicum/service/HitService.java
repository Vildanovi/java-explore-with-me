package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndPointHitDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitService {

    private final HitRepository hitRepository;

    public Hit createHit(EndPointHitDto endPointHitDto) {
        return hitRepository.save(HitMapper.mapEndPointHitDtoToHit(endPointHitDto));

    }
}
