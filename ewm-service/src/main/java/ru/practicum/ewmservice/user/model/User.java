package ru.practicum.ewmservice.user.model;

import lombok.*;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.request.model.ParticipationRequest;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users", schema = "PUBLIC")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 255, nullable = false)
    private String name;
    @Column(length = 800, nullable = false)
    private String email;
    @OneToMany
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private List<ParticipationRequest> participationRequestList;
    @OneToMany(mappedBy = "initiator")
    private List<Event> ownedEvents;
}
