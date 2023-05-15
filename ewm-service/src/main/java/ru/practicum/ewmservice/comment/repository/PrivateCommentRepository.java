package ru.practicum.ewmservice.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.comment.model.Comment;

@Repository
public interface PrivateCommentRepository extends JpaRepository<Comment, Long> {

}
