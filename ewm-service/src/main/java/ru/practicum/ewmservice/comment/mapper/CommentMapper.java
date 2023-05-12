package ru.practicum.ewmservice.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.comment.DTO.CommentFullDTO;
import ru.practicum.ewmservice.comment.DTO.CommentShortDTO;
import ru.practicum.ewmservice.comment.DTO.NewCommentDTO;
import ru.practicum.ewmservice.comment.model.Comment;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommentMapper {

    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    public CommentMapper(UserMapper userMapper, EventMapper eventMapper) {
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;
    }

    public Comment mapNewToEntity(NewCommentDTO newCommentDTO, User author, Event event) {
        Comment comment = Comment.builder()
                .author(author)
                .event(event)
                .text(newCommentDTO.getText())
                .created(LocalDateTime.now())
                .build();
        return comment;
    }

    public CommentFullDTO mapEntityToFullDTO(Comment comment) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CommentFullDTO commentFullDTO = CommentFullDTO.builder()
                .id(comment.getId())
                .author(userMapper.mapShortDTOFromUser(comment.getAuthor()))
                .eventShortDTO(eventMapper.mapEntityToShortDTO(comment.getEvent()))
                .text(comment.getText())
                .created(comment.getCreated().format(dateTimeFormatter))
                .build();
        return commentFullDTO;
    }

    public CommentShortDTO mapEntityToShortDTO(Comment comment) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CommentShortDTO commentShortDTO = CommentShortDTO.builder()
                .id(comment.getId())
                .author(userMapper.mapShortDTOFromUser(comment.getAuthor()))
                .text(comment.getText())
                .created(comment.getCreated().format(dateTimeFormatter))
                .build();
        return commentShortDTO;
    }
}
