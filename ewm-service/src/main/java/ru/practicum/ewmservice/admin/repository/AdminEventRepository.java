package ru.practicum.ewmservice.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.event.model.Event;

import java.util.List;

@Repository
public interface AdminEventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByCategory(Category category);
}
