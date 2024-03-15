package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {

    boolean existsEventsByCategoryId(int id);

    Set<Event> findAllByIdIn(Set<Integer> ids);

    Optional<Event> findByIdAndInitiator_Id(Integer eventId, Integer userId);

    List<Event> findAllByInitiatorId(Integer userId, Pageable pageable);

}
