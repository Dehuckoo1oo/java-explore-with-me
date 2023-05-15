package ru.practicum.ewmservice.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.comment.DTO.CommentFullDTO;
import ru.practicum.ewmservice.comment.DTO.NewCommentDTO;
import ru.practicum.ewmservice.comment.DTO.UpdateCommentDTO;
import ru.practicum.ewmservice.comment.service.PrivateCommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class PrivateCommentController {
    private PrivateCommentService privateCommentService;

    public PrivateCommentController(PrivateCommentService privateCommentService) {
        this.privateCommentService = privateCommentService;
    }

    @PostMapping("/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDTO createComment(@PathVariable Integer userId, @PathVariable Integer eventId,
                                        @RequestBody @Valid NewCommentDTO newCommentDTO) {
        return privateCommentService.createComment(newCommentDTO, userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDTO updateComment(@PathVariable Integer userId, @PathVariable Integer eventId,
                                        @PathVariable Long commentId,
                                        @RequestBody @Valid UpdateCommentDTO updateCommentDTO) {
        return privateCommentService.updateComment(updateCommentDTO, userId, eventId, commentId);
    }

    @DeleteMapping("/{userId}/events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommentFullDTO updateComment(@PathVariable Integer userId, @PathVariable Integer eventId,
                                        @PathVariable Long commentId) {
        return privateCommentService.removeComment(userId,eventId,commentId);
    }

}
