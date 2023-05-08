package ru.practicum.ewmservice.event.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;
import ru.practicum.ewmservice.event.enums.EventSortType;
import ru.practicum.ewmservice.event.enums.EventState;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.PublicEventRepository;
import ru.practicum.ewmservice.exception.NoSuchBodyException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PublicEventServiceImpl implements PublicEventService {
    private final PublicEventRepository publicEventRepository;
    private final EventMapper eventMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public PublicEventServiceImpl(PublicEventRepository publicEventRepository, EventMapper eventMapper) {
        this.publicEventRepository = publicEventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventShortDTO> getEvents(String text, List<Integer> categoryIds, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable,
                                         EventSortType sort, Integer size, Integer from) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        Predicate criteria = builder.conjunction();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (text != null) {
            Predicate annotation = builder.like(builder.lower(event.get("annotation")),
                    "%" + text.toLowerCase() + "%");
            Predicate description = builder.like(builder.lower(event.get("description")),
                    "%" + text.toLowerCase() + "%");
            Predicate hasText = builder.or(annotation, description);
            criteria = builder.and(criteria, hasText);
        }
        if (categoryIds != null) {
            Predicate inCategories = event.get("category").in(categoryIds);
            criteria = builder.and(criteria, inCategories);
        }
        if (paid != null) {
            Predicate paidStat = event.get("paid").in(paid);
            criteria = builder.and(criteria, paidStat);
        }
        if (rangeStart != null) {
            LocalDateTime localDateTimeStart = LocalDateTime.parse(rangeStart, dateTimeFormatter);
            Predicate start = builder.greaterThan(event.get("eventDate"), localDateTimeStart);
            criteria = builder.and(criteria, start);
        } else {
            Predicate start = builder.greaterThan(event.get("eventDate"), LocalDateTime.now());
            criteria = builder.and(criteria, start);
        }
        if (rangeEnd != null) {
            LocalDateTime localDateTimeStart = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
            Predicate end = builder.lessThan(event.get("eventDate"), localDateTimeStart);
            criteria = builder.and(criteria, end);
        } else {
            Predicate start = builder.greaterThan(event.get("eventDate"), LocalDateTime.now().plusYears(1));
            criteria = builder.and(criteria, start);
        }
        Predicate statsEvent = event.get("state").in(EventState.PUBLISHED);
        criteria = builder.and(criteria, statsEvent);
        query.select(event).where(criteria);
        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from).setMaxResults(size).getResultList();
        List<EventShortDTO> eventShortDTOs = events.stream().map(eventMapper::mapEntityToShortDTO)
                .collect(Collectors.toList());
        if (sort != null && sort.equals(EventSortType.VIEWS)) {
            eventShortDTOs.sort(Comparator.comparing(EventShortDTO::getViews));
        } else {
            eventShortDTOs.sort(Comparator.comparing(EventShortDTO::getEventDate));
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
