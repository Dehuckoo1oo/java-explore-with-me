package ru.practicum.ewmservice.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.event.enums.EventState;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.PrivateEventRepository;
import ru.practicum.ewmservice.exception.ConflictException;
import ru.practicum.ewmservice.exception.NoSuchBodyException;
import ru.practicum.ewmservice.exception.NotFoundResourceException;
import ru.practicum.ewmservice.request.DTO.ParticipationRequestDTO;
import ru.practicum.ewmservice.request.enums.RequestStatus;
import ru.practicum.ewmservice.request.mapper.RequestMapper;
import ru.practicum.ewmservice.request.model.ParticipationRequest;
import ru.practicum.ewmservice.request.repository.PrivateRequestRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final PrivateRequestRepository privateRequestRepository;
    private final PrivateEventRepository privateEventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    public PrivateRequestServiceImpl(PrivateRequestRepository privateRequestRepository, UserRepository userRepository,
                                     PrivateEventRepository privateEventRepository, RequestMapper requestMapper) {
        this.privateRequestRepository = privateRequestRepository;
        this.privateEventRepository = privateEventRepository;
        this.userRepository = userRepository;
        this.requestMapper = requestMapper;
    }

    @Override
    public ParticipationRequestDTO createRequest(Integer userId, Integer eventId) {
        User user = getUser(userId);
        Event event = privateEventRepository.findById(eventId).orElseThrow(() ->
                new NoSuchBodyException(String.format("Event with id=%s was not found", eventId)));
        long duplicate = event.getParticipationRequests().stream().filter(request -> request.getRequester()
                .equals(user)).count();
        if (duplicate > 0) {
            throw new ConflictException("Error: Request already exists");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Error: Can't request owned Event");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Error: Event not published");
        }
        long activeRequestCount = event.getParticipationRequests().stream()
                .filter(request -> request.getRequestStatus().equals(RequestStatus.CONFIRMED)).count();
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= activeRequestCount) {
            throw new ConflictException("Error: Request limit exceeded");
        }
        ParticipationRequest participationRequest;
        if (event.getRequestModeration()) {
            participationRequest = ParticipationRequest.builder()
                    .requestStatus(RequestStatus.PENDING)
                    .created(LocalDateTime.now())
                    .requester(user)
                    .event(event)
                    .build();
        } else {
            participationRequest = ParticipationRequest.builder()
                    .requestStatus(RequestStatus.CONFIRMED)
                    .created(LocalDateTime.now())
                    .requester(user)
                    .event(event)
                    .build();
        }
        ParticipationRequest participationRequest1 = privateRequestRepository.save(participationRequest);
        return requestMapper.mapEntityToDTO(participationRequest1);
    }

    @Override
    public ParticipationRequestDTO cancelRequest(Integer userId, Integer requestId) {
        User user = getUser(userId);
        ParticipationRequest participationRequest = privateRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundResourceException(String.format("Request with id=%s was not found", requestId)));
        if (!participationRequest.getRequester().equals(user)) {
            throw new NotFoundResourceException(
                    String.format("User with id=%s not created request with id=%s ", userId, requestId));
        }
        participationRequest.setRequestStatus(RequestStatus.CANCELED);
        return requestMapper.mapEntityToDTO(privateRequestRepository.save(participationRequest));
    }

    @Override
    public List<ParticipationRequestDTO> getRequestByUserId(Integer userId) {
        User user = getUser(userId);
        List<ParticipationRequest> requests = privateRequestRepository.findAllByRequester(user);
        return requests.stream().map(requestMapper::mapEntityToDTO).collect(Collectors.toList());
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundResourceException(String.format("User with id=%s was not found", userId)));
    }

}
