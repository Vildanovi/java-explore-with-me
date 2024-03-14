package ru.practicum.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.category.CategoryDto;
import ru.practicum.stats.dto.category.NewCategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.service.CategoriesService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin: Категории", description = "API для работы с категориями")
public class CategoriesAdminController {

    private final CategoriesService categoriesService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(
            summary = "Добавление новой категории",
            description = "Обратите внимание: имя категории должно быть уникальным"
    )
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.mapNewCategoryDtoToCategories(newCategoryDto);
        return CategoryMapper.mapCategoriesToCategoryDto(categoriesService.createCategory(category));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удаление категории",
            description = "Обратите внимание: с категорией не должно быть связано ни одного события."
    )
    public void deleteCategory(@PathVariable @Positive int catId) {
        categoriesService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    @Operation(
            summary = "Изменение категории",
            description = "Обратите внимание: имя категории должно быть уникальным"
    )
    public CategoryDto updateCategory(@PathVariable @Positive int catId,
                                      @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return CategoryMapper.mapCategoriesToCategoryDto(categoriesService.updateCategory(catId, newCategoryDto));
    }
}
