package ru.practicum.ewmservice.comment.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.comment.DTO.CommentShortDTO;
import ru.practicum.ewmservice.comment.service.PublicCommentService;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}")
public class PublicCommentController {
    private PublicCommentService publicCommentService;

    public PublicCommentController(PublicCommentService publicCommentService) {
        this.publicCommentService = publicCommentService;
    }

    @GetMapping("/comments")
    public List<CommentShortDTO> getCommentsByEvent(@PathVariable Integer eventId,
                                                    @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                    @RequestParam(name = "from", required = false, defaultValue = "0") int from) {
        return publicCommentService.findCommentsByEvent(eventId, from, size);
    }

}
