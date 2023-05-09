package ru.practicum.ewmservice.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.repository.PublicCategoryRepository;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.NewEventDTO;
import ru.practicum.ewmservice.event.DTO.UpdateEventDTO;
import ru.practicum.ewmservice.event.enums.EventState;
import ru.practicum.ewmservice.event.enums.EventStateAction;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.mapper.LocationMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.event.repository.PrivateEventRepository;
import ru.practicum.ewmservice.exception.ConflictException;
import ru.practicum.ewmservice.exception.IncorrectlyException;
import ru.practicum.ewmservice.exception.NoSuchBodyException;
import ru.practicum.ewmservice.request.DTO.ParticipationRequestDTO;
import ru.practicum.ewmservice.request.enums.RequestStatus;
import ru.practicum.ewmservice.request.mapper.RequestMapper;
import ru.practicum.ewmservice.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.request.model.ParticipationRequest;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PrivateEventServiceImpl implements PrivateEventService {
    private final PrivateEventRepository privateEventRepository;
    private final UserRepository userRepository;
    private final PublicCategoryRepository publicCategoryRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;

    public PrivateEventServiceImpl(PrivateEventRepository privateEventRepository, UserRepository userRepository,
                                   PublicCategoryRepository publicCategoryRepository, EventMapper eventMapper,
                                   LocationMapper locationMapper, RequestMapper requestMapper) {
        this.privateEventRepository = privateEventRepository;
        this.userRepository = userRepository;
        this.publicCategoryRepository = publicCategoryRepository;
        this.eventMapper = eventMapper;
        this.locationMapper = locationMapper;
        this.requestMapper = requestMapper;
    }

    @Override
    public EventFullDTO createEvent(Integer userId, NewEventDTO newEventDTO) {
        checkDate(newEventDTO.getEventDate());
        Category category = getCategory(newEventDTO.getCategory());
        Location location = locationMapper.mapDTOToEntity(newEventDTO.getLocation());
        User initiator = getUser(userId);
        Event event = eventMapper.mapNewToEntity(newEventDTO, initiator, category, location);
        Event savedEvent = privateEventRepository.save(event);
        EventFullDTO eventFullDTO = eventMapper.mapEntityToFullDTO(savedEvent);
        return eventFullDTO;
    }

    @Override
    public EventFullDTO updateEvent(Integer userId, Integer eventId, UpdateEventDTO updateEventDTO) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new IncorrectlyException(String.format("Failed: userId. Error:пользователь не является владельцем " +
                    "события Value:%s", userId));
        }
        LocalDateTime eventDate;
        Category category;
        Location location;
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Изменить можно только отмененные или не опубликованные ивенты");
        }
        if (updateEventDTO.getEventDate() != null) {
            eventDate = LocalDateTime.parse(updateEventDTO.getEventDate(), dateTimeFormatter);
            LocalDateTime nowDate = LocalDateTime.now().plusHours(2);
            if (eventDate.isBefore(nowDate)) {
                throw new ConflictException(String.format("Failed: eventDate. Error: должно содержать дату, " +
                        "которая еще не наступила Value:%s", eventDate));
            }
        } else {
            eventDate = event.getEventDate();
        }

        if (updateEventDTO.getCategory() != null) {
            category = getCategory(updateEventDTO.getCategory());
        } else {
            category = event.getCategory();
        }

        if (updateEventDTO.getLocation() != null) {
            location = locationMapper.mapDTOToEntity(updateEventDTO.getLocation());
        } else {
            location = event.getLocation();
        }

        if (updateEventDTO.getStateAction() != null) {
            EventStateAction state;
            try {
                state = EventStateAction.valueOf(updateEventDTO.getStateAction());
            } catch (IllegalArgumentException e) {
                throw new IncorrectlyException("Указан некорректный статус");
            }
            if (state.equals(EventStateAction.SEND_TO_REVIEW)) {
                if (event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.REJECTED)) {
                    event.setState(EventState.PENDING);
                } else {
                    throw new IncorrectlyException("Указан некорректный статус");
                }
            } else if (state.equals(EventStateAction.CANCEL_REVIEW)) {
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.CANCELED);
                } else {
                    throw new IncorrectlyException("Указан некорректный статус");
                }
            } else {
                throw new IncorrectlyException("Указан некорректный статус");
            }
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
        return eventMapper.mapEntityToFullDTO(privateEventRepository.save(event));
    }

    @Override
    public List<EventFullDTO> findEvents(Integer userId, Integer from, Integer size) {
        List<Event> events = privateEventRepository.findEventsByUserId(userId, PageRequest.of(from, size));
        return events.stream().map(eventMapper::mapEntityToFullDTO).collect(Collectors.toList());
    }

    @Override
    public EventFullDTO findEvent(Integer userId, Integer eventId) {
        Event event = getEvent(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            return eventMapper.mapEntityToFullDTO(event);
        } else {
            throw new IncorrectlyException(String.format("Failed: userId. Error:пользователь не является владельцем " +
                    "события Value:%s", userId));
        }
    }

    @Override
    public List<ParticipationRequestDTO> getRequestsByUserEvent(Integer userId, Integer eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new NoSuchBodyException(String.format("User with id=%s not owned event with id=%s", userId, eventId));
        }
        List<ParticipationRequest> requests = event.getParticipationRequests();
        List<ParticipationRequestDTO> requestsDTO = requests.stream().map(requestMapper::mapEntityToDTO)
                .collect(Collectors.toList());
        return requestsDTO;
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestByUserEvent(Integer userId, Integer eventId,
                                                                   EventRequestStatusUpdateRequest
                                                                           eventRequestStatusUpdateRequest) {
        RequestStatus requiredRequestStatus = RequestStatus.valueOf(eventRequestStatusUpdateRequest.getStatus());
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new NoSuchBodyException(String.format("User with id=%s not owned event with id=%s", userId, eventId));
        }
        Integer requestLimit = event.getParticipantLimit();
        if (!event.getRequestModeration() || requestLimit < 1) {
            throw new ConflictException("No confirmation required");
        }

        long countConfirmedRequest = event.getParticipationRequests().stream()
                .filter(request -> request.getRequestStatus().equals(RequestStatus.CONFIRMED)).count();

        if (requestLimit < countConfirmedRequest) {
            throw new ConflictException("Error: Requests out of limit");
        }
        Map<Integer, ParticipationRequest> requests = new HashMap<>();
        event.getParticipationRequests().forEach(request -> requests.put(request.getId(), request));
        for (Integer requestId : eventRequestStatusUpdateRequest.getRequestIds()) {
            if (countConfirmedRequest >= requestLimit) {
                rejectAllPendingRequests(event);
                privateEventRepository.save(event);
                throw new ConflictException("Error: Requests out of limit");
            }
            ParticipationRequest participationRequest = requests.get(requestId);
            if (!participationRequest.getRequestStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictException("Error: status can be changed only on PENDING request. Current status" +
                        participationRequest.getRequestStatus());
            }
            participationRequest.setRequestStatus(requiredRequestStatus);
            if (requiredRequestStatus.equals(RequestStatus.CONFIRMED)) {
                countConfirmedRequest++;
            }
        }
        privateEventRepository.save(event);
        return requestMapper.mapResultByEvent(event);
    }

    private void rejectAllPendingRequests(Event event) {
        event.getParticipationRequests().stream()
                .filter(request -> request.getRequestStatus().equals(RequestStatus.PENDING))
                .forEach(request -> request.setRequestStatus(RequestStatus.REJECTED));
    }

    private Category getCategory(Integer catId) {
        return publicCategoryRepository.findById(catId).orElseThrow(() -> new NoSuchBodyException(
                String.format("Category with id=%s was not found", catId)));
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchBodyException(
                String.format("User with id=%s was not found", userId)));
    }

    private Event getEvent(Integer eventId) {
        return privateEventRepository.findById(eventId).orElseThrow(() ->
                new NoSuchBodyException(String.format("Event with id=%s was not found", eventId)));
    }

    private void checkDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime eventDate = LocalDateTime.parse(date, dateTimeFormatter);
        LocalDateTime nowDate = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(nowDate)) {
            throw new ConflictException(String.format("Failed: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила Value:%s", eventDate));
        }
    }

}
