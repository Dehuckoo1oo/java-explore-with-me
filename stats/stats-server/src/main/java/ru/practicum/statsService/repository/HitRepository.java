package ru.practicum.statsService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.statsService.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Integer> {
    @Query("SELECT h FROM Hit h WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris ORDER BY h.app, h.uri")
    List<Hit> findByTimestampBetweenAndUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                            @Param("uris") List<String> uris);


    @Query(value = "select h2.id, h2.app, h2.uri, h2.ip, h2.timestamp " +
            "from (select h.id " +
            ", h.app " +
            ", h.uri " +
            ", h.ip " +
            ", h.timestamp " +
            ", row_number() over (partition by h.app, h.uri, h.ip order by timestamp) as rn " +
            "from public.hits h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris ORDER BY h.uri, h.timestamp) h2 " +
            "where rn = 1 order by h2.app, h2.uri; ", nativeQuery = true)
    List<Hit> findByTimestampBetweenAndUrisUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                                  @Param("uris") List<String> uris);

    @Query(value = "select h2.id, h2.app, h2.uri, h2.ip, h2.timestamp " +
            "from (select h.id " +
            ", h.app " +
            ", h.uri " +
            ", h.ip " +
            ", h.timestamp " +
            ", row_number() over (partition by h.app, h.uri, h.ip order by timestamp) as rn " +
            "from public.hits h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "ORDER BY h.uri, h.timestamp) h2 " +
            "where rn = 1 order by h2.app, h2.uri; ", nativeQuery = true)
    List<Hit> findByTimestampBetweenUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT h FROM Hit h WHERE h.timestamp BETWEEN :start AND :end ORDER BY h.app, h.uri")
    List<Hit> findByTimestampBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

