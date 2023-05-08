package ru.practicum.ewmservice.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.request.DTO.ParticipationRequestDTO;
import ru.practicum.ewmservice.request.enums.RequestStatus;
import ru.practicum.ewmservice.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.request.model.ParticipationRequest;
import ru.practicum.ewmservice.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public ParticipationRequest mapDTOToEntity(ParticipationRequestDTO participationRequestDTO, Event event,
                                               User requester) {
        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .requestStatus(RequestStatus.valueOf(participationRequestDTO.getStatus()))
                .build();
        return participationRequest;
    }

    public ParticipationRequestDTO mapEntityToDTO(ParticipationRequest participationRequest) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ParticipationRequestDTO participationRequestDTO = ParticipationRequestDTO.builder()
                .created(participationRequest.getCreated().format(dateTimeFormatter))
                .status(participationRequest.getRequestStatus().toString())
                .requester(participationRequest.getRequester().getId())
                .id(participationRequest.getId())
                .event(participationRequest.getEvent().getId())
                .build();
        return participationRequestDTO;
    }

    public EventRequestStatusUpdateResult mapResultByEvent(Event event) {
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(event.getParticipationRequests().stream()
                        .filter(request -> request.getRequestStatus().equals(RequestStatus.CONFIRMED))
                        .map(this::mapEntityToDTO)
                        .collect(Collectors.toList()))
                .rejectedRequest(event.getParticipationRequests().stream()
                        .filter(request -> request.getRequestStatus().equals(RequestStatus.REJECTED))
                        .map(this::mapEntityToDTO)
                        .collect(Collectors.toList()))
                .build();
        return eventRequestStatusUpdateResult;
    }
}
