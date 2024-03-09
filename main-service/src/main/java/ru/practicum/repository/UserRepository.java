package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Users;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>, QuerydslPredicateExecutor<Users> {

    List<Users> findAllByIdIn(List<Integer> ids, Pageable pageable);

}
