package ru.practicum.ewmservice.comment.service;

import ru.practicum.ewmservice.comment.DTO.CommentShortDTO;

import java.util.List;

public interface PublicCommentService {

    List<CommentShortDTO> findCommentsByEvent(Integer eventId, Integer from, Integer size);
}
