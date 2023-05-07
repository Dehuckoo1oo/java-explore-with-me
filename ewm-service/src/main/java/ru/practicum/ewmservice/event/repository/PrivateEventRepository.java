package ru.practicum.ewmservice.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.model.Event;

import java.util.List;

@Repository
public interface PrivateEventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e WHERE e.initiator.id = :userId")
    List<Event> findEventsByUserId(@Param("userId") Integer userId, Pageable pageable);
}
