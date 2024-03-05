package ru.practicum.controller.pub;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.service.CompilationsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
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
//        if (pinned == null) {
//            return compilationsService.getAll(from, size);
//        }

        return compilationsService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @Operation(
            summary = "Получение подборки событий по его id",
            description = "В случае, если подборки с заданным id не найдено, возвращает статус код 404"
    )
    public CompilationDto getCompilationById(@PathVariable @Positive int compId) {
        return compilationsService.getCompilationById(compId);
    }
}
