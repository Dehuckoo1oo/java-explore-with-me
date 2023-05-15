package ru.practicum.ewmservice.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.comment.model.Comment;


@Repository
public interface AdminCommentRepository extends JpaRepository<Comment, Long> {
}
