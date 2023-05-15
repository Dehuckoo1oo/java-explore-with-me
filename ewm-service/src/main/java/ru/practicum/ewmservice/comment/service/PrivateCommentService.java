package ru.practicum.ewmservice.comment.service;

import ru.practicum.ewmservice.comment.DTO.CommentFullDTO;
import ru.practicum.ewmservice.comment.DTO.NewCommentDTO;
import ru.practicum.ewmservice.comment.DTO.UpdateCommentDTO;


public interface PrivateCommentService {

    CommentFullDTO createComment(NewCommentDTO newCommentDTO, Integer userId, Integer eventId);

    CommentFullDTO updateComment(UpdateCommentDTO updateCommentDTO, Integer userId, Integer eventId,
                                 Long commentId);

    CommentFullDTO removeComment(Integer userId, Integer eventId, Long commentId);
}
