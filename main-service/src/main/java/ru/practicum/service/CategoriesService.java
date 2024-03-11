package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.constant.Constants;
import ru.practicum.pageable.OffsetBasedPageRequest;
import ru.practicum.stats.dto.category.NewCategoryDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.ValidationBadRequestException;
import ru.practicum.model.Category;
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
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(int catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new EntityNotFoundException("Объект не найден: " + catId);
        }
        if (eventRepository.existsEventsByCategoryId(catId)) {
            throw new ValidationBadRequestException("Найдены события с указанной категорией: " + catId);
        }
        categoryRepository.deleteById(catId);
    }

    @Transactional
    public Category updateCategory(int catId, NewCategoryDto newCategoryDto) {
        Category updateCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + catId));
        updateCategory.setName(newCategoryDto.getName());
        return updateCategory;
    }

    public List<Category> getCategories(Integer from, Integer size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size, Constants.SORT_ASC_ID);
        return categoryRepository.findAll(pageable).getContent();
    }

    public Category getCategoryById(@PathVariable @Positive int catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + catId));
    }
}
