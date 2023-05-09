package ru.practicum.ewmservice.request.DTO;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequestDTO {
    private Integer id;
    private String created;
    private Integer event;
    private Integer requester;
    private String status;
}
