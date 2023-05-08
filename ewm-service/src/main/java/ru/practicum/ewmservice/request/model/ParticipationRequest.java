package ru.practicum.ewmservice.request.model;

import lombok.*;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.request.enums.RequestStatus;
import ru.practicum.ewmservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "requests", schema = "PUBLIC")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;
    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
}
