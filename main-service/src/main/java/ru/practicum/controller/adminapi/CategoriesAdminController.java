package ru.practicum.controller.adminapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.CategoriesService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin")
@Tag(name = "Admin: Категории", description = "API для работы с категориями")
public class CategoriesAdminController {

    private final CategoriesService categoriesService;

    @PostMapping("/categories")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(
            summary = "Добавление новой категории",
            description = "Обратите внимание: имя категории должно быть уникальным"
    )
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoriesService.createCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удаление категории",
            description = "Обратите внимание: с категорией не должно быть связано ни одного события."
    )
    public void deleteCategory(@PathVariable @Positive int catId) {
        categoriesService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    @Operation(
            summary = "Изменение категории",
            description = "Обратите внимание: имя категории должно быть уникальным"
    )
    public CategoryDto updateCategory(@PathVariable @Positive int catId,
                                      @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoriesService.updateCategory(catId, newCategoryDto);
    }
}
