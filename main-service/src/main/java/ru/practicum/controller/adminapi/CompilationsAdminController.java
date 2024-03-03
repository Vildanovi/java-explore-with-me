package ru.practicum.controller.adminapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilations.CompilationsResponseDto;
import ru.practicum.dto.compilations.NewCompilationsDto;
import ru.practicum.dto.compilations.UpdateCompilationsDto;
import ru.practicum.mapper.CompilationsMapper;
import ru.practicum.service.CompilationsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin")
@Tag(name = "Admin: Подборки событий", description = "API для работы с подборками событий")
public class CompilationsAdminController {

    private final CompilationsService compilationsService;

    @PostMapping("/compilations")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Добавление новой подборки (подборка может не содержать событий)")
    public CompilationsResponseDto createCompilations(@RequestBody @Valid NewCompilationsDto newCompilationsDto) {
        return CompilationsMapper
                .mapCompilationsDtoToCompilationsResponseDto(compilationsService
                        .createCompilations(newCompilationsDto));
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление подборки")
    public void deleteCompilations(@PathVariable @Positive int compId) {
        compilationsService.deleteCompilations(compId);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Обновить информацию о подборке")
    public CompilationsResponseDto putCompilations(@PathVariable @Positive int compId,
                                                   @RequestBody @Valid UpdateCompilationsDto updateCompilationsDto) {
        return CompilationsMapper
                .mapCompilationsDtoToCompilationsResponseDto(compilationsService
                        .putCompilations(compId, updateCompilationsDto));
    }
}
