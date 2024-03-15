package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Users;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserRepository extends JpaRepository<Users, Integer>, QuerydslPredicateExecutor<Users> {

    List<Users> findAllByIdIn(List<Integer> ids, Pageable pageable);

    boolean existsUsersByEmailIsIgnoreCase(String name);
}
