package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.NewCompilationsDto;
import ru.practicum.dto.compilations.UpdateCompilationsDto;
import ru.practicum.repository.CompilationsRepository;

import javax.validation.constraints.Positive;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationsService {

    private final CompilationsRepository compilationsRepository;

    public CompilationDto createCompilations(NewCompilationsDto newCompilationsDto) {
        return null;
    }

    public void deleteCompilations(int compId) {
    }

    public CompilationDto putCompilations(int compId,
                                          UpdateCompilationsDto updateCompilationsDto) {
        return null;
    }

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, @Positive Integer size) {
        return null;
    }

    public CompilationDto getCompilationById(@PathVariable @Positive int compId) {
        return null;
    }
}
