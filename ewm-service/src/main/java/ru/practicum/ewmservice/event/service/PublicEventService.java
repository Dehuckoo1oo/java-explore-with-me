package ru.practicum.ewmservice.event.service;

import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.event.DTO.EventShortDTO;
import ru.practicum.ewmservice.event.enums.EventSortType;

import java.util.List;

public interface PublicEventService {

    List<EventShortDTO> getEvents(String text, List<Integer> categoryIds, Boolean paid, String rangeStart,
                                  String rangeEnd, Boolean onlyAvailable, EventSortType sort, Integer size,
                                  Integer from);

    EventFullDTO getEventById(Integer eventId);
}
