package ru.practicum.ewmservice.comment.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.comment.DTO.CommentFullDTO;
import ru.practicum.ewmservice.comment.DTO.NewCommentDTO;
import ru.practicum.ewmservice.comment.DTO.UpdateCommentDTO;
import ru.practicum.ewmservice.comment.mapper.CommentMapper;
import ru.practicum.ewmservice.comment.model.Comment;
import ru.practicum.ewmservice.comment.repository.PrivateCommentRepository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.PrivateEventRepository;
import ru.practicum.ewmservice.exception.ConflictException;
import ru.practicum.ewmservice.exception.NoSuchBodyException;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;


@Service
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private PrivateCommentRepository privateCommentRepository;
    private UserRepository userRepository;
    private PrivateEventRepository privateEventRepository;
    private CommentMapper commentMapper;

    public PrivateCommentServiceImpl(PrivateCommentRepository privateCommentRepository, UserRepository userRepository,
                                     PrivateEventRepository privateEventRepository, CommentMapper commentMapper) {
        this.privateCommentRepository = privateCommentRepository;
        this.userRepository = userRepository;
        this.privateEventRepository = privateEventRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentFullDTO createComment(NewCommentDTO newCommentDTO, Integer userId, Integer eventId) {
        User author = checkUser(userId);
        Event event = checkEvent(eventId);
        Comment comment = commentMapper.mapNewToEntity(newCommentDTO, author, event);
        Comment savedComment = privateCommentRepository.save(comment);
        return commentMapper.mapEntityToFullDTO(savedComment);
    }

    @Override
    public CommentFullDTO updateComment(UpdateCommentDTO updateCommentDTO, Integer userId, Integer eventId,
                                        Long commentId) {
        Comment comment = checkComment(commentId);
        User user = checkUser(userId);
        checkEvent(eventId);
        if (!comment.getAuthor().equals(user)) {
            throw new ConflictException(String.format("User with id=%s not owned comment with id=%s", userId,
                    commentId));
        }
        if (!comment.getEvent().getId().equals(eventId)) {
            throw new ConflictException(String.format("Comment with id=%s not from event with id=%s", commentId,
                    eventId));
        }
        comment.setText(updateCommentDTO.getText());
        Comment savedComment = privateCommentRepository.save(comment);
        return commentMapper.mapEntityToFullDTO(savedComment);
    }

    @Override
    public CommentFullDTO removeComment(Integer userId, Integer eventId, Long commentId) {
        User user = checkUser(userId);
        checkEvent(eventId);
        Comment comment = checkComment(commentId);
        if (!comment.getAuthor().equals(user)) {
            throw new ConflictException(String.format("User with id=%s not owned comment with id=%s", userId,
                    commentId));
        }

        if (!comment.getEvent().getId().equals(eventId)) {
            throw new ConflictException(String.format("Comment with id=%s not from event with id=%s", commentId,
                    eventId));
        }
        privateCommentRepository.delete(comment);
        return commentMapper.mapEntityToFullDTO(comment);
    }


    private User checkUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchBodyException(
                String.format("User with id=%s was not found", userId)));
        return user;
    }

    private Event checkEvent(Integer eventId) {
        Event event = privateEventRepository.findById(eventId).orElseThrow(() -> new NoSuchBodyException(
                String.format("Event with id=%s was not found", eventId)));
        return event;
    }

    private Comment checkComment(Long commentId) {
        Comment comment = privateCommentRepository.findById(commentId).orElseThrow(() -> new NoSuchBodyException(
                String.format("Comment with id=%s was not found", commentId)));
        return comment;
    }
}
