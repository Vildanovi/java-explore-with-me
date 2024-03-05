package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Event;

import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer> {

    boolean existsEventsByCategoryId(int id);

    Set<Event> findAllByIdIn(Set<Integer> ids);
}
