package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.CompilationsResponseDto;

@UtilityClass
public class CompilationsMapper {

    public CompilationsResponseDto mapCompilationsDtoToCompilationsResponseDto(CompilationDto compilationDto) {
        return CompilationsResponseDto.builder()
                .id(compilationDto.getId())
                .title(compilationDto.getTitle())
                .events(compilationDto.getEvents())
                .build();
    }
}
