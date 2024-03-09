package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;
import ru.practicum.model.enumerations.StateEvent;

import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {

    boolean existsEventsByCategoryId(int id);

    Set<Event> findAllByIdIn(Set<Integer> ids);

    Event findByIdAndStateIs(Integer id, StateEvent state);
}
