package ru.practicum.ewmservice.admin.DTO;

import lombok.*;
import ru.practicum.ewmservice.event.DTO.LocationDTO;
import ru.practicum.ewmservice.event.enums.EventStateAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequest {
    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private LocationDTO location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    private String title;
}
