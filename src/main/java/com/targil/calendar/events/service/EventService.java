package com.targil.calendar.events.service;

import com.targil.calendar.events.model.dto.CreateEventDTO;
import com.targil.calendar.events.model.dto.ResponseEventDTO;
import com.targil.calendar.events.model.dto.ResponseEventsPageDTO;
import com.targil.calendar.events.model.entity.EventEntity;

public interface EventService {
    ResponseEventDTO findEventById(Long id);
    ResponseEventDTO createEvent(CreateEventDTO event);
    ResponseEventDTO updateEvent(Long id, EventEntity event);
    void deleteEvent(Long id);
    ResponseEventsPageDTO findEvents(String location, String sortBy, String order, int pageSize, int size);
}
