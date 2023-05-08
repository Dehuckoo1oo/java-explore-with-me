package ru.practicum.ewmservice.event.service;

import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.NewEventDTO;
import ru.practicum.ewmservice.event.DTO.UpdateEventDTO;
import ru.practicum.ewmservice.request.DTO.ParticipationRequestDTO;
import ru.practicum.ewmservice.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.request.model.EventRequestStatusUpdateResult;

import java.util.List;

public interface PrivateEventService {
    EventFullDTO createEvent(Integer userId, NewEventDTO newEventDTO);

    EventFullDTO updateEvent(Integer userId, Integer eventId, UpdateEventDTO updateEventDTO);

    List<EventFullDTO> findEvents(Integer userId, Integer from, Integer size);

    EventFullDTO findEvent(Integer userId, Integer eventId);

    List<ParticipationRequestDTO> getRequestsByUserEvent(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateRequestByUserEvent(Integer userId, Integer eventId,
                                                            EventRequestStatusUpdateRequest
                                                                    eventRequestStatusUpdateRequest);
}
