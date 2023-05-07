package ru.practicum.ewmservice.admin.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.admin.DTO.UpdateEventAdminRequest;
import ru.practicum.ewmservice.admin.repository.AdminCategoryRepository;
import ru.practicum.ewmservice.admin.repository.AdminEventRepository;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.enums.EventState;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.mapper.LocationMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.event.repository.LocationRepository;
import ru.practicum.ewmservice.exception.IncorrectlyException;
import ru.practicum.ewmservice.exception.NoSuchBodyException;
import ru.practicum.ewmservice.exception.NotFoundResourceException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminEventServiceImpl implements AdminEventService {

    private final AdminEventRepository adminEventRepository;
    private final AdminCategoryRepository adminCategoryRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public AdminEventServiceImpl(AdminEventRepository adminEventRepository, EventMapper eventMapper,
                                 AdminCategoryRepository adminCategoryRepository, LocationMapper locationMapper) {
        this.adminEventRepository = adminEventRepository;
        this.eventMapper = eventMapper;
        this.adminCategoryRepository = adminCategoryRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<EventFullDTO> getEvents(List<Long> ids, List<String> states, List<Integer> categories,
                                        String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime startDt = null;
        LocalDateTime endDt = null;
        List<EventState> eventStates = new ArrayList<>();
        if (states != null) {
            eventStates = states.stream().map(EventState::valueOf).collect(Collectors.toList());
        }
        if (rangeStart != null) {
            startDt = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        }
        if (rangeEnd != null) {
            endDt = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }

        List<Event> events = adminEventRepository.findEvents(ids, eventStates, categories, startDt, endDt,
                PageRequest.of(from, size));
        return events.stream().map(eventMapper::mapEntityToFullDTO).collect(Collectors.toList());
    }

    @Override
    public EventFullDTO updateEvent(Integer eventId, UpdateEventAdminRequest updateEventDTO) {
        Event event = adminEventRepository.findById(eventId).orElseThrow(() ->
                new NoSuchBodyException(String.format("Event with id=%s was not found", eventId)));
        LocalDateTime eventDate;
        Category category;
        Location location;
        if (updateEventDTO.getEventDate() != null) {
            eventDate = LocalDateTime.parse(updateEventDTO.getEventDate(), dateTimeFormatter);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new IncorrectlyException(String.format("Failed: eventDate. Error: должно содержать дату, " +
                        "которая еще не наступила Value:%s", eventDate));
            }
        } else {
            throw new NotFoundResourceException(String.format("Failed: eventDate. Error: must not be blank " +
                    "Value:%s", updateEventDTO.getEventDate()));
        }

        if (updateEventDTO.getCategory() != null) {
            category = adminCategoryRepository.findById(updateEventDTO.getCategory())
                    .orElseThrow(() -> new IncorrectlyException(String.format("Failed: category. " +
                            "Error: не найдена категория. Value:%s", updateEventDTO.getCategory())));
        } else {
            throw new NotFoundResourceException(String.format("Failed: category. Error: must not be blank " +
                    "Value:%s", updateEventDTO.getCategory()));
        }

        if (updateEventDTO.getLocation() != null) {
            location = locationMapper.mapDTOToEntity(updateEventDTO.getLocation());
        } else {
            location = event.getLocation();
        }

        try {
            EventState state = EventState.valueOf(updateEventDTO.getStateAction());
            if (state.equals(EventState.PUBLISHED) && event.getState().equals(EventState.PENDING)) {
                event.setState(EventState.PUBLISHED);
            } else if (state.equals(EventState.CANCELED) && !event.getState().equals(EventState.PUBLISHED)) {
                event.setState(EventState.CANCELED);
            } else {
                throw new IncorrectlyException("Указан некорректный статус");
            }
        } catch (IllegalArgumentException e) {
            throw new IncorrectlyException("Указан некорректный статус");
        }

        if (updateEventDTO.getAnnotation() != null) {
            event.setAnnotation(updateEventDTO.getAnnotation());
        }
        event.setCategory(category);
        if (updateEventDTO.getDescription() != null) {
            event.setDescription(updateEventDTO.getDescription());
        }
        event.setEventDate(eventDate);
        event.setLocation(location);
        if (updateEventDTO.getPaid() != null) {
            event.setPaid(updateEventDTO.getPaid());
        }
        if (updateEventDTO.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDTO.getParticipantLimit());
        }
        if (updateEventDTO.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDTO.getRequestModeration());
        }
        if (updateEventDTO.getTitle() != null) {
            event.setTitle(updateEventDTO.getTitle());
        }
        return eventMapper.mapEntityToFullDTO(adminEventRepository.save(event));
    }

}
