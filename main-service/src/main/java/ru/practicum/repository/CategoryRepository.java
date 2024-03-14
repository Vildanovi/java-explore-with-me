package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>, QuerydslPredicateExecutor<Category> {

    @Query("select (count(c) > 0) from Category c where upper(c.name) = upper(?1)")
    boolean existUniqueName(String name);

    @Query("select (count(c) > 0) from Category c where upper(c.name) = upper(?1)")
    boolean existCategoryName(String name);


}
