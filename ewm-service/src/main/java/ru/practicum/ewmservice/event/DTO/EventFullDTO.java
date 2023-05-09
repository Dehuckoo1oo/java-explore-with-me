package ru.practicum.ewmservice.event.DTO;

import lombok.*;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.user.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDTO {
    private Integer id;
    private String annotation;
    private InnerCategory category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private InnerInitiator initiator;
    private LocationDTO location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Integer views;

    @Getter
    @Setter
    public static class InnerInitiator {
        private Integer id;
        private String name;

        public InnerInitiator(User user) {
            this.id = user.getId();
            this.name = user.getName();
        }
    }

    @Getter
    @Setter
    public static class InnerCategory {
        private Integer id;
        private String name;

        public InnerCategory(Category category) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}
