package ru.practicum.ewmservice.comment.DTO;

import lombok.*;
import ru.practicum.ewmservice.user.model.User;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentShortDTO {
    private Long id;
    private InnerAuthor author;
    private CommentFullDTO.InnerEventShortDTO eventShortDTO;
    private String text;
    private String created;

    @Getter
    @Setter
    public static class InnerAuthor {
        private Integer id;
        private String name;

        public InnerAuthor(User user) {
            this.id = user.getId();
            this.name = user.getName();
        }
    }

}
