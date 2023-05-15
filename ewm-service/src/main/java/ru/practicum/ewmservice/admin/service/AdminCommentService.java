package ru.practicum.ewmservice.admin.service;

import ru.practicum.ewmservice.comment.DTO.CommentFullDTO;


public interface AdminCommentService {
    CommentFullDTO findCommentById(Long id);

    CommentFullDTO removeCommentById(Long id);
}
