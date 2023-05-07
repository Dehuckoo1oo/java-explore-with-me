package ru.practicum.ewmservice.event.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;
import ru.practicum.ewmservice.event.enums.EventSortType;
import ru.practicum.ewmservice.event.service.PublicEventService;
import ru.practicum.statsClient.StatsClient;
import ru.practicum.statsDTO.HitDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService publicEventService;
    private final StatsClient statsClient;

    public PublicEventController(PublicEventService publicEventService, StatsClient statsClient) {
        this.publicEventService = publicEventService;
        this.statsClient = statsClient;
    }

    @GetMapping
    public List<EventShortDTO> getEvents(@RequestParam(name = "text", required = false) String text,
                                         @RequestParam(name = "categories", required = false) List<Integer> categoryIds,
                                         @RequestParam(name = "paid", required = false) Boolean paid,
                                         @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                         @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                         @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false")
                                             Boolean onlyAvailable,
                                         @RequestParam(name = "sort", required = false) EventSortType sort,
                                         @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                         @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                         HttpServletRequest request) {
        statsClient.hit(new HitDTO("ewm-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().toString()));
        return publicEventService.getEvents(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, size, from);
    }

    @GetMapping("/{id}")
    public EventFullDTO getEventById(@PathVariable("id") Integer eventId,
                                     HttpServletRequest request) {
        statsClient.hit(new HitDTO("ewm-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().toString()));
        return publicEventService.getEventById(eventId);
    }
}
