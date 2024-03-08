package ru.practicum.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.compilations.CompilationDto;
import ru.practicum.stats.dto.compilations.NewCompilationsDto;
import ru.practicum.stats.dto.compilations.UpdateCompilationRequest;
import ru.practicum.mapper.CompilationsMapper;
import ru.practicum.service.CompilationsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin: Подборки событий", description = "API для работы с подборками событий")
public class CompilationsAdminController {

    private final CompilationsService compilationsService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Добавление новой подборки (подборка может не содержать событий)")
    public CompilationDto createCompilations(@RequestBody @Valid NewCompilationsDto newCompilationsDto) {
        return CompilationsMapper
                .mapCompilationToCompilationDto(compilationsService
                        .createCompilations(newCompilationsDto));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление подборки")
    public void deleteCompilations(@PathVariable @Positive int compId) {
        compilationsService.deleteCompilations(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Обновить информацию о подборке")
    public CompilationDto putCompilations(@PathVariable @Positive int compId,
                                          @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        return CompilationsMapper
                .mapCompilationToCompilationDto(compilationsService
                        .putCompilations(compId, updateCompilationRequest));
    }
}
