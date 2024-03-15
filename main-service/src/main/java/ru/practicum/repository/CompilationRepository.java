package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer>, QuerydslPredicateExecutor<Compilation> {

    List<Compilation> findAllByPinnedIs(boolean pinned, Pageable page);
}
