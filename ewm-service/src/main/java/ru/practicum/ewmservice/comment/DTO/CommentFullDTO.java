package ru.practicum.ewmservice.comment.DTO;

import lombok.*;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;
import ru.practicum.ewmservice.user.model.User;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentFullDTO {
    private Long id;
    private InnerAuthor author;
    private InnerEventShortDTO eventShortDTO;
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

    @Getter
    @Setter
    public static class InnerEventShortDTO {
        private Integer id;
        private String annotation;
        private EventShortDTO.InnerCategory category;
        private Long confirmedRequests;
        private String eventDate;
        private EventShortDTO.InnerInitiator initiator;
        private Boolean paid;
        private String title;
        private Integer views;

        public InnerEventShortDTO(EventShortDTO eventShortDTO) {
            this.id = eventShortDTO.getId();
            this.annotation = eventShortDTO.getAnnotation();
            this.category = eventShortDTO.getCategory();
            this.confirmedRequests = eventShortDTO.getConfirmedRequests();
            this.eventDate = eventShortDTO.getEventDate();
            this.initiator = eventShortDTO.getInitiator();
            this.paid = eventShortDTO.getPaid();
            this.title = eventShortDTO.getTitle();
            this.views = eventShortDTO.getViews();
        }
    }

}
