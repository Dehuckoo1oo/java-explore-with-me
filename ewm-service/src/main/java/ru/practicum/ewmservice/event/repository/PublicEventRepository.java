package ru.practicum.ewmservice.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PublicEventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT e FROM Event e WHERE (:text IS NULL OR e.title LIKE %:text%) AND " +
            "(:categories IS NULL OR e.category.id IN :categories) AND " +
            "(:paid IS NULL OR e.paid = :paid) AND " +
            "(:rangeStart IS NULL OR e.eventDate >= :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate <= :rangeEnd) AND " +
            "(:onlyAvailable = false OR (e.participantLimit IS NULL OR " +
            "e.participantLimit > (SELECT COUNT(r) FROM ParticipationRequest r WHERE r.event.id = e.id)))")
    List<Event> findEvents(@Param("text") String text,
                           @Param("categories") List<Integer> categoryIds,
                           @Param("paid") Boolean paid,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd,
                           @Param("onlyAvailable") Boolean onlyAvailable,
                           Pageable pageable);
}
