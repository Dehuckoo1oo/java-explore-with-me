package ru.practicum.statsDTO;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"hits"})
@AllArgsConstructor
@NoArgsConstructor
public class StatsDTO {
    private String app;
    private String uri;
    private Integer hits;
}
