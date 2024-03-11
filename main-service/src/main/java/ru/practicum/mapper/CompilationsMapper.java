package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stats.dto.compilations.CompilationDto;
import ru.practicum.stats.dto.compilations.CompilationsResponseDto;
import ru.practicum.stats.dto.compilations.NewCompilationsDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationsMapper {

    public CompilationsResponseDto mapCompilationsDtoToCompilationsResponseDto(CompilationDto compilationDto) {
        return CompilationsResponseDto.builder()
                .id(compilationDto.getId())
                .title(compilationDto.getTitle())
                .events(compilationDto.getEvents())
                .build();
    }

    public Compilation mapNewCompilationsDtoToCompilation(NewCompilationsDto newCompilationsDto, Set<Event> events) {
        return Compilation.builder()
                .pinned(newCompilationsDto.isPinned())
                .title(newCompilationsDto.getTitle())
                .events(events)
                .build();
    }

    public CompilationDto mapCompilationToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents()
                        .stream()
                        .map(EventMapper::mapEventToEventShortDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
