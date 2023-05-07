package ru.practicum.ewmservice.compilation.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationDTO {
    private List<Integer> events;
    private Boolean pinned;
    private String title;
}
