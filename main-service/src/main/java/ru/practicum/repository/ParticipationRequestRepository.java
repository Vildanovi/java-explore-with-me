package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.enumerations.RequestStatus;
import ru.practicum.stats.dto.request.RequestConfirmedCountDto;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer>, QuerydslPredicateExecutor<ParticipationRequest> {

    int countAllByEvent_IdAndStatusIs(Integer eventId, RequestStatus status);

    List<ParticipationRequest> findAllByEventId(Integer id);

    List<ParticipationRequest> findAllByIdIn(List<Integer> ids);

    List<ParticipationRequest> findAllByRequesterId(Integer id);

    @Query("SELECT new ru.practicum.stats.dto.request.RequestConfirmedCountDto(pr.event.id, COUNT(pr)) " +
            "FROM ParticipationRequest AS pr " +
            "WHERE pr.event.id IN ?1 and pr.status = ?2 group by pr.event.id")
    List<RequestConfirmedCountDto> findByEventAndStartAndEnd(List<Integer> ids, RequestStatus status);

    Optional<ParticipationRequest> findByEvent_IdAndRequester_Id(Integer eventId, Integer requestId);


    List<ParticipationRequest> findAllByRequesterIdAndEventId(Integer userId, Integer eventId);
}
