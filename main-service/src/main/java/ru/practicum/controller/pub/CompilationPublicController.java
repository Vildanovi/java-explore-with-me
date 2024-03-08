package ru.practicum.controller.pub;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.compilations.CompilationDto;
import ru.practicum.mapper.CompilationsMapper;
import ru.practicum.service.CompilationsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@Tag(name = "Public: Подборки событий", description = "Публичный API для работы с подборками событий")
public class CompilationPublicController {

    private final CompilationsService compilationsService;

    @GetMapping
    @Operation(
            summary = "Получение подборок событий",
            description = "В случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список"
    )
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        return compilationsService.getCompilations(pinned, from, size)
                .stream()
                .map(CompilationsMapper::mapCompilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{compId}")
    @Operation(
            summary = "Получение подборки событий по его id",
            description = "В случае, если подборки с заданным id не найдено, возвращает статус код 404"
    )
    public CompilationDto getCompilationById(@PathVariable @Positive int compId) {
        return CompilationsMapper
                .mapCompilationToCompilationDto(compilationsService
                        .getCompilationById(compId));
    }
}
