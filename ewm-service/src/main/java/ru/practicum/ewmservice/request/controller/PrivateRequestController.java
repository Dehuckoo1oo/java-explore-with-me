package ru.practicum.ewmservice.request.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.request.DTO.ParticipationRequestDTO;
import ru.practicum.ewmservice.request.service.PrivateRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {
    private final PrivateRequestService privateRequestService;

    public PrivateRequestController(PrivateRequestService privateRequestService) {
        this.privateRequestService = privateRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDTO createRequest(@PathVariable Integer userId,
                                                 @RequestParam(name = "eventId") Integer eventId) {
        return privateRequestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDTO cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        return privateRequestService.cancelRequest(userId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDTO> getRequestByUserId(@PathVariable Integer userId) {
        return privateRequestService.getRequestByUserId(userId);
    }


}
