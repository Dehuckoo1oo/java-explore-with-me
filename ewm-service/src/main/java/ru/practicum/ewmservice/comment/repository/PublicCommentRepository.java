package ru.practicum.ewmservice.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.comment.model.Comment;

import java.util.List;

@Repository
public interface PublicCommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByEventId(Integer eventId, Pageable pageable);
}
