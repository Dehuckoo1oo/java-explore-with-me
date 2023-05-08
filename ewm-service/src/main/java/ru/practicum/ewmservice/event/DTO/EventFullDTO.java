package ru.practicum.ewmservice.event.DTO;

import lombok.*;
import ru.practicum.ewmservice.category.DTO.CategoryDTO;
import ru.practicum.ewmservice.user.DTO.UserShortDTO;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDTO {
    private Integer id;
    private String annotation;
    private CategoryDTO category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDTO initiator;
    private LocationDTO location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Integer views;
}
