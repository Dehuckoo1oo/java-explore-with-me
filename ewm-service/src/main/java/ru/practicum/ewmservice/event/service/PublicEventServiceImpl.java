package ru.practicum.ewmservice.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;
import ru.practicum.ewmservice.event.enums.EventSortType;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.PublicEventRepository;
import ru.practicum.ewmservice.exception.NoSuchBodyException;
import ru.practicum.statsClient.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PublicEventServiceImpl implements PublicEventService {
    private final PublicEventRepository publicEventRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsClient statsClient;
    private final EventMapper eventMapper;

    public PublicEventServiceImpl(PublicEventRepository publicEventRepository, StatsClient statsClient,
                                  EventMapper eventMapper) {
        this.publicEventRepository = publicEventRepository;
        this.statsClient = statsClient;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventShortDTO> getEvents(String text, List<Integer> categoryIds, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, EventSortType sort, int size,
                                         int from) {
        LocalDateTime startDt = null;
        if (rangeStart != null) {
            startDt = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        }
        LocalDateTime endDt = null;
        if (rangeEnd != null) {
            endDt = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }
        List<Event> events = publicEventRepository.findEvents(text, categoryIds, paid, startDt, endDt, onlyAvailable,
                PageRequest.of(from, size));
        if (sort.equals(EventSortType.EVENT_DATE)) {
            events.sort(Comparator.comparing(Event::getEventDate));
        }
        List<EventShortDTO> eventShortDTOs = events.stream().map(eventMapper::mapEntityToShortDTO).collect(Collectors.toList());
        List<String> listIds = eventShortDTOs.stream().map(event -> event.getId().toString()).collect(Collectors.toList());
        Map<Integer, Integer> stats = statsClient.getStats(null, null, listIds, false);
        eventShortDTOs.forEach(dto -> dto.setViews(stats.get(dto.getId())));
        if (sort.equals(EventSortType.VIEWS)) {
            eventShortDTOs.sort(Comparator.comparing(EventShortDTO::getViews));
        }
        return eventShortDTOs;
    }

    @Override
    public EventFullDTO getEventById(Integer eventId) {
        Event event = publicEventRepository.findById(eventId).orElseThrow(() ->
                new NoSuchBodyException(String.format("Event with id=%s was not found", eventId)));
        return eventMapper.mapEntityToFullDTO(event);
    }
}
