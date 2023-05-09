package ru.practicum.ewmservice.user.model;

import lombok.*;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.request.model.ParticipationRequest;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users", schema = "PUBLIC")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 800, nullable = false)
    private String email;
    @OneToMany
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private List<ParticipationRequest> participationRequestList;
    @OneToMany(mappedBy = "initiator")
    private List<Event> ownedEvents;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

}
