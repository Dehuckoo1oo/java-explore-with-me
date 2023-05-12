package ru.practicum.ewmservice.admin.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.admin.repository.AdminCommentRepository;
import ru.practicum.ewmservice.comment.DTO.CommentFullDTO;
import ru.practicum.ewmservice.comment.mapper.CommentMapper;
import ru.practicum.ewmservice.comment.model.Comment;
import ru.practicum.ewmservice.exception.NoSuchBodyException;


@Service
public class AdminCommentServiceImpl implements AdminCommentService {
    private AdminCommentRepository adminCommentRepository;
    private CommentMapper commentMapper;

    public AdminCommentServiceImpl(AdminCommentRepository adminCommentRepository, CommentMapper commentMapper) {
        this.adminCommentRepository = adminCommentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentFullDTO removeCommentById(Long commentId) {
        Comment comment = checkComment(commentId);
        adminCommentRepository.delete(comment);
        return commentMapper.mapEntityToFullDTO(comment);
    }

    @Override
    public CommentFullDTO findCommentById(Long commentId) {
        Comment comment = checkComment(commentId);
        return commentMapper.mapEntityToFullDTO(comment);
    }

    private Comment checkComment(Long commentId) {
        Comment comment = adminCommentRepository.findById(commentId).orElseThrow(() -> new NoSuchBodyException(
                String.format("Comment with id=%s was not found", commentId)));
        return comment;
    }
}
