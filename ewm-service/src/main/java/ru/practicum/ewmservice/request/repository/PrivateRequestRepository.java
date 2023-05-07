package ru.practicum.ewmservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.request.model.ParticipationRequest;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;

@Repository
public interface PrivateRequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    public List<ParticipationRequest> findAllByRequester(User requester);
}
