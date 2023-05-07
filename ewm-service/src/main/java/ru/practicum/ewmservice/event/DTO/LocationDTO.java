package ru.practicum.ewmservice.event.DTO;

import lombok.*;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    //@NotNull
    private Double lat;
    //@NotNull
    private Double lon;
}
