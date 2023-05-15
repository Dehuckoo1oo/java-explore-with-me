package ru.practicum.ewmservice.comment.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.comment.DTO.CommentShortDTO;
import ru.practicum.ewmservice.comment.mapper.CommentMapper;
import ru.practicum.ewmservice.comment.model.Comment;
import ru.practicum.ewmservice.comment.repository.PublicCommentRepository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.PublicEventRepository;
import ru.practicum.ewmservice.exception.NoSuchBodyException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicCommentServiceImpl implements PublicCommentService {
    private PublicCommentRepository publicCommentRepository;
    private PublicEventRepository publicEventRepository;
    private CommentMapper commentMapper;

    public PublicCommentServiceImpl(PublicCommentRepository publicCommentRepository, CommentMapper commentMapper,
                                    PublicEventRepository publicEventRepository) {
        this.publicCommentRepository = publicCommentRepository;
        this.publicEventRepository = publicEventRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public List<CommentShortDTO> findCommentsByEvent(Integer eventId, Integer from, Integer size) {
        Event event = checkEvent(eventId);
        List<Comment> comments = publicCommentRepository.findCommentsByEventId(eventId, PageRequest.of(from, size));
        List<CommentShortDTO> commentShortDTOs = comments.stream().map(commentMapper::mapEntityToShortDTO)
                .collect(Collectors.toList());
        return commentShortDTOs;
    }

    private Event checkEvent(Integer eventId) {
        Event event = publicEventRepository.findById(eventId).orElseThrow(() -> new NoSuchBodyException(
                String.format("Event with id=%s was not found", eventId)));
        return event;
    }
}
