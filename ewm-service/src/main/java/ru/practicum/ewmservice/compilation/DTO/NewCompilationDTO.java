package ru.practicum.ewmservice.compilation.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDTO {
    private List<Integer> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
