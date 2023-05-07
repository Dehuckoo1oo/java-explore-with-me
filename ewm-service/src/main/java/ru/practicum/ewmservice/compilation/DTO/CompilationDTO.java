package ru.practicum.ewmservice.compilation.DTO;

import lombok.*;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDTO {
    private Integer id;
    private Boolean pinned;
    private String title;
    private Set<EventShortDTO> events;
}
