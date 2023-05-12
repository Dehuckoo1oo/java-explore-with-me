package ru.practicum.ewmservice.comment.service;

import ru.practicum.ewmservice.comment.DTO.CommentFullDTO;
import ru.practicum.ewmservice.comment.DTO.CommentShortDTO;
import ru.practicum.ewmservice.comment.DTO.NewCommentDTO;
import ru.practicum.ewmservice.comment.DTO.UpdateCommentDTO;

import java.util.List;

public interface PrivateCommentService {

    CommentFullDTO createComment(NewCommentDTO newCommentDTO, Integer userId, Integer eventId);
    CommentFullDTO updateComment(UpdateCommentDTO updateCommentDTO, Integer userId, Integer eventId,
                                 Long commentId);
    CommentFullDTO removeComment(Integer userId, Integer eventId, Long commentId);
}
