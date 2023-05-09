package ru.practicum.statsServer.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "hits", schema = "PUBLIC")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 8000, nullable = false)
    private String app;
    @Column(length = 8000)
    private String uri;
    @Column(length = 8000, nullable = false)
    private String ip;
    private LocalDateTime timestamp;
}
