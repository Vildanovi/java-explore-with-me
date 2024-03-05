package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.ValidationBadRequestException;
import ru.practicum.model.Categories;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriesService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    public Categories createCategory(Categories categories) {
        return categoryRepository.save(categories);
    }

    public void deleteCategory(int catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new EntityNotFoundException("Объект не найден: " + catId);
        }
        if (eventRepository.existsEventsByCategoryId(catId)) {
            throw new ValidationBadRequestException("Найдены события с указанной категорией: " + catId);
        }
        categoryRepository.deleteById(catId);
    }

    public Categories updateCategory(int catId, NewCategoryDto newCategoryDto) {
        Categories updateCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + catId));
        updateCategory.setName(newCategoryDto.getName());
        return updateCategory;
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return null;
    }

    public CategoryDto getCategoryById(@PathVariable @Positive int catId) {
        return null;
    }
}
