package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.enumerations.RequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer>, QuerydslPredicateExecutor<ParticipationRequest> {

    int countAllByEvent_IdAndStatusIs(Integer eventId, RequestStatus status);

    List<ParticipationRequest> findAllByEventId(Integer id);

    List<ParticipationRequest> findAllByIdIn(List<Integer> ids);

    List<ParticipationRequest> findAllByRequesterId(Integer id);

    @Query("SELECT pr.event.id, COUNT(pr.event.id) " +
            "FROM ParticipationRequest AS pr " +
            "WHERE pr.event.id IN ?1 and pr.status = ?2 group by pr.event.id")
    Map<Integer, Integer> findByEventAndStartAndEnd(List<Integer> ids, RequestStatus status);

    @Query("select r.event.id, count(r.id) from ParticipationRequest r where r.event.id in ?1 and r.status = ?2 group by r.event.id")
    Map<Integer, Integer> findAllConfirmedRequestsByEventIds(List<Integer> ids, RequestStatus status);

    @Query("select p from ParticipationRequest p where p.event.id in ?1 and p.status = ?2")
    List<ParticipationRequest> findByEvent_IdInAndStatus(Collection<Integer> ids, RequestStatus status);


//    List<ParticipationRequest> findByEvent_IdInAndStatus(Collection<Integer> ids, RequestStatus status);




    @Query("select count(r.id) from ParticipationRequest r where r.event.id = ?1 and r.status = ?2")
    int findAllConfirmedRequestsByEventId(Integer id, RequestStatus status);

    List<ParticipationRequest> findAllByRequesterIdAndEventId(Integer userId, Integer eventId);
}
