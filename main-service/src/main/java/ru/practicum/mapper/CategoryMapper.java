package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stats.dto.category.CategoryDto;
import ru.practicum.stats.dto.category.NewCategoryDto;
import ru.practicum.model.Category;

@UtilityClass
public class CategoryMapper {

    public Category mapNewCategoryDtoToCategories(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryDto mapCategoriesToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
