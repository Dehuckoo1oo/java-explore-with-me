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
public class EventShortDTO {
    private Integer id;
    private String annotation;
    private CategoryDTO category;
    private Long confirmedRequests;
    private String eventDate;
    private UserShortDTO initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
