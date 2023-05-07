package ru.practicum.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.NewEventDTO;
import ru.practicum.ewmservice.event.DTO.UpdateEventDTO;
import ru.practicum.ewmservice.event.service.PrivateEventService;
import ru.practicum.ewmservice.request.DTO.ParticipationRequestDTO;
import ru.practicum.ewmservice.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.request.model.EventRequestStatusUpdateResult;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDTO> getEvents(@PathVariable Integer userId,
                                        @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                        @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return privateEventService.findEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDTO createEvent(@PathVariable("userId") Integer userId,
                                    @RequestBody @Valid NewEventDTO newEventDto) {
        EventFullDTO eventFullDTO = privateEventService.createEvent(userId, newEventDto);
        return eventFullDTO;
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDTO getEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return privateEventService.findEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDTO updateEvent(@PathVariable Integer userId, @PathVariable Integer eventId,
                                    @RequestBody UpdateEventDTO updateEventDTO) {
        return privateEventService.updateEvent(userId, eventId, updateEventDTO);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDTO> getRequestsByUserEvent(@PathVariable Integer userId,
                                                                @PathVariable Integer eventId) {
        return privateEventService.getRequestsByUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestByUserEvent(@PathVariable Integer userId,
                                                                   @PathVariable Integer eventId,
                                                                   @RequestBody EventRequestStatusUpdateRequest
                                                                           eventRequestStatusUpdateRequest) {
        return privateEventService.updateRequestByUserEvent(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
