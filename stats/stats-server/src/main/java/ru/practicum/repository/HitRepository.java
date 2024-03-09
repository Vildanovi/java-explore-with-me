package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.model.ViewStats(v.app, v.uri, COUNT(v.ip)) " +
            "FROM EndpointHit AS v WHERE v.timestamp BETWEEN :start AND :end " +
            "GROUP BY v.app, v.uri ORDER BY COUNT(v.ip) DESC")
    List<ViewStats> findByStartAndEnd(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(v.app, v.uri, COUNT(DISTINCT v.ip)) " +
            "FROM EndpointHit AS v WHERE v.timestamp BETWEEN :start AND :end " +
            "GROUP BY v.app, v.uri ORDER BY COUNT(DISTINCT v.ip) DESC")
    List<ViewStats> findUniqueByStartAndEnd(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(v.app, v.uri, COUNT(v.ip)) " +
            "FROM EndpointHit AS v WHERE v.timestamp BETWEEN :start AND :end " +
            "AND v.uri IN :uris GROUP BY v.app, v.uri ORDER BY COUNT(v.ip) DESC")
    List<ViewStats> findByUrisAndStartAndEnd(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(v.app, v.uri, COUNT(DISTINCT v.ip)) " +
            "FROM EndpointHit AS v WHERE v.timestamp BETWEEN :start AND :end " +
            "AND v.uri IN :uris GROUP BY v.app, v.uri ORDER BY COUNT(DISTINCT v.ip) DESC")
    List<ViewStats> findUniqueByUrisAndStartAndEnd(@Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end,
                                                   @Param("uris") List<String> uris);

    @Query(value =
            "SELECT app AS app, uri AS uri, COUNT(ip) AS ip FROM EndpointHit" +
                    " WHERE uri IN :uris AND (timestamp >= :start AND timestamp <= :end) GROUP BY app, uri"

    )
    List<ViewStats> findAllByUris(@Param("uris") List<String> uris,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);

    @Query(value =
            "SELECT app AS app, uri AS uri, COUNT(DISTINCT ip) AS ip FROM EndpointHit" +
                    " WHERE uri IN :uris AND (timestamp >= :start AND timestamp < :end) GROUP BY app, uri"
    )
    List<ViewStats> findAllUnique(@Param("uris") List<String> uris,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);

}
