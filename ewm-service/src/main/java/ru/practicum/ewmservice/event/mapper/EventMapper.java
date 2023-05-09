package ru.practicum.ewmservice.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;
import ru.practicum.ewmservice.event.DTO.NewEventDTO;
import ru.practicum.ewmservice.event.enums.EventState;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.request.enums.RequestStatus;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.statsClient.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper {
    private final LocationMapper locationMapper;
    private final StatsClient statsClient;

    public EventMapper(LocationMapper locationMapper, StatsClient statsClient) {
        this.locationMapper = locationMapper;
        this.statsClient = statsClient;
    }

    public Event mapNewToEntity(NewEventDTO newEventDTO, User initiator, Category category, Location location) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EventState eventState;
        LocalDateTime publishedOn = LocalDateTime.now();
        eventState = EventState.PENDING;
        LocalDateTime timestamp = LocalDateTime.now();
        Event event = Event.builder()
                .annotation(newEventDTO.getAnnotation())
                .category(category)
                .participationRequests(new ArrayList<>())
                .createdOn(timestamp)
                .description(newEventDTO.getDescription())
                .eventDate(LocalDateTime.parse(newEventDTO.getEventDate(), dateTimeFormatter))
                .initiator(initiator)
                .location(location)
                .paid(newEventDTO.getPaid())
                .participantLimit(newEventDTO.getParticipantLimit())
                .publishedOn(publishedOn)
                .requestModeration(newEventDTO.getRequestModeration())
                .state(eventState)
                .title(newEventDTO.getTitle())
                .build();
        return event;
    }

    public EventFullDTO mapEntityToFullDTO(Event event) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Integer stats = statsClient.getStats(null, null, List.of(event.getId().toString()), false)
                .getOrDefault(event.getId(), 0);
        EventFullDTO eventFullDTO = EventFullDTO.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new EventFullDTO.InnerCategory(event.getCategory()))
                .confirmedRequests(event.getParticipationRequests().stream().filter(req -> req.getRequestStatus()
                        .equals(RequestStatus.CONFIRMED)).count())
                .createdOn(event.getCreatedOn().format(dateTimeFormatter))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(dateTimeFormatter))
                .initiator(new EventFullDTO.InnerInitiator(event.getInitiator()))
                .location(locationMapper.mapEntityToDTO(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn().format(dateTimeFormatter))
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(stats)
                .build();
        return eventFullDTO;
    }

    public EventShortDTO mapEntityToShortDTO(Event event) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Integer stats = statsClient.getStats(null, null, List.of(event.getId().toString()), false)
                .getOrDefault(event.getId(), 0);
        EventShortDTO eventShortDTO = EventShortDTO.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new EventShortDTO.InnerCategory(event.getCategory()))
                .confirmedRequests(event.getParticipationRequests().stream().filter(req -> req.getRequestStatus()
                        .equals(RequestStatus.CONFIRMED)).count())
                .eventDate(event.getEventDate().format(dateTimeFormatter))
                .initiator(new EventShortDTO.InnerInitiator(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(stats)
                .build();
        return eventShortDTO;
    }
}
