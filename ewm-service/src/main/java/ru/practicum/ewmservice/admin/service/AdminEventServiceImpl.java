package ru.practicum.ewmservice.admin.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.admin.DTO.UpdateEventAdminRequest;
import ru.practicum.ewmservice.admin.repository.AdminCategoryRepository;
import ru.practicum.ewmservice.admin.repository.AdminEventRepository;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.enums.EventState;
import ru.practicum.ewmservice.event.enums.EventStateAction;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.mapper.LocationMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.exception.ConflictException;
import ru.practicum.ewmservice.exception.IncorrectlyException;
import ru.practicum.ewmservice.exception.NoSuchBodyException;
import ru.practicum.statsClient.StatsClient;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminEventServiceImpl implements AdminEventService {

    private final AdminEventRepository adminEventRepository;
    private final AdminCategoryRepository adminCategoryRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    @PersistenceContext
    private EntityManager entityManager;
    private final StatsClient statsClient;

    public AdminEventServiceImpl(AdminEventRepository adminEventRepository, EventMapper eventMapper,
                                 AdminCategoryRepository adminCategoryRepository, LocationMapper locationMapper,
                                 StatsClient statsClient) {
        this.adminEventRepository = adminEventRepository;
        this.eventMapper = eventMapper;
        this.adminCategoryRepository = adminCategoryRepository;
        this.locationMapper = locationMapper;
        this.statsClient = statsClient;
    }

    @Override
    public List<EventFullDTO> getEvents(List<Integer> ids, List<String> states, List<Integer> categories,
                                        String rangeStart, String rangeEnd, Integer from, Integer size) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        Predicate criteria = builder.conjunction();
        if (ids != null) {
            Predicate inUsers = event.get("initiator").in(ids);
            criteria = builder.and(criteria, inUsers);
        }
        if (states != null) {
            List<EventState> eventStates = states.stream().map(EventState::valueOf).collect(Collectors.toList());
            Predicate inStates = event.get("state").in(eventStates);
            criteria = builder.and(criteria, inStates);
        }
        if (categories != null) {
            Predicate inCategories = event.get("category").in(categories);
            criteria = builder.and(criteria, inCategories);
        }
        if (rangeStart != null) {
            LocalDateTime localDateTimeStart = LocalDateTime.parse(rangeStart, dateTimeFormatter);
            Predicate start = builder.greaterThan(event.get("eventDate"), localDateTimeStart);
            criteria = builder.and(criteria, start);
        }
        if (rangeEnd != null) {
            LocalDateTime localDateTimeStart = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
            Predicate end = builder.lessThan(event.get("eventDate"), localDateTimeStart);
            criteria = builder.and(criteria, end);
        }

        query.select(event).where(criteria);
        List<Event> events = entityManager.createQuery(query).setFirstResult(from).setMaxResults(size).getResultList();

        List<EventFullDTO> result = events.stream().map(eventMapper::mapEntityToFullDTO).collect(Collectors.toList());

        return result;
    }

    @Override
    public EventFullDTO updateEvent(Integer eventId, UpdateEventAdminRequest updateEventDTO) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Event event = adminEventRepository.findById(eventId).orElseThrow(() ->
                new NoSuchBodyException(String.format("Event with id=%s was not found", eventId)));
        LocalDateTime eventDate;
        Category category;
        Location location;
        if (updateEventDTO.getEventDate() != null) {
            eventDate = LocalDateTime.parse(updateEventDTO.getEventDate(), dateTimeFormatter);
            LocalDateTime nowDate = LocalDateTime.now().plusHours(1);
            if (eventDate.isBefore(nowDate)) {
                throw new ConflictException(String.format("Failed: eventDate. Error: должно содержать дату, " +
                        "которая еще не наступила Value:%s", eventDate));
            }
        } else {
            eventDate = event.getEventDate();
        }
        if (updateEventDTO.getCategory() != null) {
            category = adminCategoryRepository.findById(updateEventDTO.getCategory())
                    .orElseThrow(() -> new IncorrectlyException(String.format("Failed: category. " +
                            "Error: не найдена категория. Value:%s", updateEventDTO.getCategory())));
        } else {
            category = event.getCategory();
        }

        if (updateEventDTO.getLocation() != null) {
            location = locationMapper.mapDTOToEntity(updateEventDTO.getLocation());
        } else {
            location = event.getLocation();
        }

        try {
            EventStateAction state = updateEventDTO.getStateAction();
            if (state.equals(EventStateAction.PUBLISH_EVENT) && (event.getState().equals(EventState.PUBLISHED))) {
                throw new ConflictException("Событие уже опубликовано");
            }
            if (state.equals(EventStateAction.PUBLISH_EVENT) && (event.getState().equals(EventState.CANCELED) ||
                    event.getState().equals(EventState.REJECTED))) {
                throw new ConflictException("Событие было отменено");
            }
            if (state.equals(EventStateAction.REJECT_EVENT) && (event.getState().equals(EventState.PUBLISHED))) {
                throw new ConflictException("Нельзя отменить опубликованное событие");
            }
            if (state.equals(EventStateAction.PUBLISH_EVENT) && (event.getState().equals(EventState.PENDING))) {
                event.setState(EventState.PUBLISHED);
            } else if (state.equals(EventStateAction.REJECT_EVENT) && !event.getState().equals(EventState.PUBLISHED)) {
                event.setState(EventState.REJECTED);
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
        if (updateEventDTO.getEventDate() != null) {
            event.setEventDate(eventDate);
        }
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
