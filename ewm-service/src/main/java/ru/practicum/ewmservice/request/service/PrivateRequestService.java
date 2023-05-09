package ru.practicum.ewmservice.request.service;

import ru.practicum.ewmservice.request.DTO.ParticipationRequestDTO;

import java.util.List;

public interface PrivateRequestService {

    ParticipationRequestDTO createRequest(Integer userId, Integer eventId);

    ParticipationRequestDTO cancelRequest(Integer userId, Integer requestId);

    List<ParticipationRequestDTO> getRequestByUserId(Integer userId);
}
