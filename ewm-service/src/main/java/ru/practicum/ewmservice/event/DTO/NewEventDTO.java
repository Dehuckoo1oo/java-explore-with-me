package ru.practicum.ewmservice.event.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class NewEventDTO {
    @Size(max = 2000,min = 20)
    @NotBlank
    private String annotation;
    @NotNull
    private Integer category;
    @Size(max = 7000,min = 20)
    @NotBlank
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private LocationDTO location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(max = 120, min = 3)
    @NotBlank
    private String title;
}
