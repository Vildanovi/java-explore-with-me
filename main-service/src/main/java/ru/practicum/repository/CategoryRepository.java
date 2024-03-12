package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, QuerydslPredicateExecutor<Category> {

    boolean existsCategoryByNameIsIgnoreCaseAndIdIsNot(String name, Integer id);

    boolean existsCategoryByNameIsIgnoreCase(String name);

    @Query("select (count(c) > 0) from Category c where upper(c.name) = upper(?1)")
    boolean existUniqueName(String name);

    @Query("select (count(c) > 0) from Category c where upper(c.name) = upper(?1) and c.id <> ?2")
    boolean existNameNotUser(String name, Integer id);

    @Query("select (count(c) > 0) from Category c where upper(c.name) = upper(?1)")
    boolean existCategoryName(String name);


}
