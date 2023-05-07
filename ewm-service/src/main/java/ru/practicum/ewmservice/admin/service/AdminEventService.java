package ru.practicum.ewmservice.admin.service;

import ru.practicum.ewmservice.admin.DTO.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;

import java.util.List;

public interface AdminEventService {
    public List<EventFullDTO> getEvents(List<Long> ids, List<String> states, List<Integer> categories,
                                        String rangeStart,String rangeEnd,int from, int size);

    public EventFullDTO updateEvent(Integer eventId, UpdateEventAdminRequest updateEventDTO);

}
