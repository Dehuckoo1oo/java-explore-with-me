package ru.practicum.ewmservice.event.DTO;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private Double lat;
    private Double lon;
}
