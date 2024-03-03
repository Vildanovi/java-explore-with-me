package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Requests;

@Repository
public interface RequestsRepository extends JpaRepository<Requests, Integer> {
}
