package ru.practicum.ewmservice.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;
import ru.practicum.ewmservice.event.DTO.NewEventDTO;
import ru.practicum.ewmservice.event.enums.EventState;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.request.enums.RequestStatus;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.statsClient.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper {
    private final LocationMapper locationMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final StatsClient statsClient;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventMapper(LocationMapper locationMapper, CategoryMapper categoryMapper, UserMapper userMapper
            , StatsClient statsClient) {
        this.locationMapper = locationMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.statsClient = statsClient;
    }

    public Event mapNewToEntity(NewEventDTO newEventDTO, User initiator, Category category, Location location) {
        EventState eventState;
        LocalDateTime publishedOn = null;
        if(newEventDTO.getRequestModeration()){
            eventState = EventState.PENDING;
        } else {
            eventState = EventState.PUBLISHED;
            publishedOn= LocalDateTime.now();
        }
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
        Integer stats = statsClient.getStats(null,null, List.of(event.getId().toString()),false)
                .getOrDefault(event.getId(),0);
        EventFullDTO eventFullDTO = EventFullDTO.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.mapEntityToDTO(event.getCategory()))
                .confirmedRequests(event.getParticipationRequests().stream().filter(req -> req.getRequestStatus()
                        .equals(RequestStatus.CONFIRMED)).count())
                .createdOn(event.getCreatedOn().format(dateTimeFormatter))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(dateTimeFormatter))
                .initiator(userMapper.mapShortDTOFromUser(event.getInitiator()))
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
        Integer stats = statsClient.getStats(null,null, List.of(event.getId().toString()),false)
                .getOrDefault(event.getId(),0);
        EventShortDTO eventShortDTO = EventShortDTO.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.mapEntityToDTO(event.getCategory()))
                .confirmedRequests(event.getParticipationRequests().stream().filter(req -> req.getRequestStatus()
                        .equals(RequestStatus.CONFIRMED)).count())
                .eventDate(event.getEventDate().format(dateTimeFormatter))
                .initiator(userMapper.mapShortDTOFromUser(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(stats)
                .build();
        return eventShortDTO;
    }
}
