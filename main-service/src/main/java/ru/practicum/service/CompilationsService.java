package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.constant.Constants;
import ru.practicum.stats.dto.compilations.NewCompilationsDto;
import ru.practicum.stats.dto.compilations.UpdateCompilationRequest;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CompilationsMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.pageable.OffsetBasedPageRequest;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationsService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public Compilation createCompilations(NewCompilationsDto newCompilationsDto) {
        Set<Integer> eventsIds = newCompilationsDto.getEvents();
        Set<Event> events = new HashSet<>();
        if (eventsIds != null) {
            events = eventRepository.findAllByIdIn(eventsIds);
        }
        return compilationRepository.save(CompilationsMapper
                .mapNewCompilationsDtoToCompilation(newCompilationsDto, events));
    }

    @Transactional
    public void deleteCompilations(int compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new EntityNotFoundException("Объект не найден: " + compId);
        }
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public Compilation putCompilations(int compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + compId));

        Set<Integer> eventsIds = updateCompilationRequest.getEvents();
        Set<Event> events;

        if (eventsIds != null) {
            events = eventRepository.findAllByIdIn(eventsIds);
            compilation.setEvents(events);
        }
        String tittle = updateCompilationRequest.getTitle();
        Boolean pinned = updateCompilationRequest.getPinned();
        if (tittle != null && !tittle.isBlank()) {
            compilation.setTitle(tittle);
        }
        if (pinned != null) {
            compilation.setPinned(pinned);
        }
        return compilation;
    }

    public List<Compilation> getCompilations(Boolean pinned, Integer from, @Positive Integer size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size, Constants.SORT_DESC_ID);
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinnedIs(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).getContent();
        }
        return compilations;
    }

    public Compilation getCompilationById(@PathVariable @Positive int compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + compId));
    }
}
