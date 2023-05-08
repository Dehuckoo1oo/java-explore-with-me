package ru.practicum.ewmservice.request.model;

import lombok.*;
import ru.practicum.ewmservice.request.DTO.ParticipationRequestDTO;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDTO> confirmedRequests;
    private List<ParticipationRequestDTO> rejectedRequests;
}
