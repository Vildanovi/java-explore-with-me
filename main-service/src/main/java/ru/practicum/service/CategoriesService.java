package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.repository.CategoriesRepository;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return null;
    }

    public void deleteCategory(int catId) {
    }

    public CategoryDto updateCategory(int catId, NewCategoryDto newCategoryDto) {
        return null;
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return null;
    }

    public CategoryDto getCategoryById(@PathVariable @Positive int catId) {
        return null;
    }
}
