package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.Categories;
import ru.practicum.model.Users;

@UtilityClass
public class CategoryMapper {

    public Categories mapNewCategoryDtoToCategories(NewCategoryDto newCategoryDto) {
        return Categories.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryDto mapCategoriesToCategoryDto(Categories categories) {
        return CategoryDto.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build();
    }
}
