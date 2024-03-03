package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Events;

public interface EventsRepository extends JpaRepository<Events, Integer> {
}
