package ru.practicum.ewmservice.comment.DTO;

import lombok.*;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;
import ru.practicum.ewmservice.user.DTO.UserShortDTO;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentShortDTO {
    private Long id;
    private UserShortDTO author;
    private EventShortDTO eventShortDTO;
    private String text;
    private String created;
}
