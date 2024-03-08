package ru.practicum.controller.pub;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.category.CategoryDto;
import ru.practicum.service.CategoriesService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Tag(name = "Public: Категории", description = "Публичный API для работы с категориями")
public class CategoryPublicController {

    private final CategoriesService categoriesService;

    @GetMapping
    @Operation(
            summary = "Получение категорий",
            description = "В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список"
    )
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        return categoriesService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    @Operation(
            summary = "Получение информации о категории по её идентификатору",
            description = "В случае, если категории с заданным id не найдено, возвращает статус код 404"
    )
    public CategoryDto getCategoryById(@PathVariable @Positive int catId) {
        return categoriesService.getCategoryById(catId);
    }
}
