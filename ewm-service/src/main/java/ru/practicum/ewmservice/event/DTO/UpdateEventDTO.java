package ru.practicum.ewmservice.event.DTO;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventDTO {
    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private LocationDTO location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}
