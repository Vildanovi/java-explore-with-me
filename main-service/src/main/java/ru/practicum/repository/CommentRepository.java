package ru.practicum.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Comments;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Integer>, QuerydslPredicateExecutor<Comments> {

    @Query("select c from Comments c where c.author.id = ?1")
    List<Comments> findByAuthor_Id(Integer id, Pageable pageable);
}
