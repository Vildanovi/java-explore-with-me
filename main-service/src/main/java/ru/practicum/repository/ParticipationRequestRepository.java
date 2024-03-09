package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.enumerations.RequestStatus;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer>, QuerydslPredicateExecutor<ParticipationRequest> {

    int countAllByEventIdAndStatusEquals(Integer eventId, RequestStatus status);

    List<ParticipationRequest> findAllByEventId(Integer id);

    List<ParticipationRequest> findAllByIdIn(List<Integer> ids);
}
