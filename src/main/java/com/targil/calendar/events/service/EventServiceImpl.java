package com.targil.calendar.events.service;

import com.targil.calendar.events.dao.EventRepository;
import com.targil.calendar.events.model.dto.CreateEventDTO;
import com.targil.calendar.events.model.dto.ResponseEventDTO;
import com.targil.calendar.events.model.dto.ResponseEventsPageDTO;
import com.targil.calendar.events.model.entity.EventEntity;
import com.targil.calendar.events.model.exception.EventNotFoundException;
import com.targil.calendar.notifications.model.EventNotification;
import com.targil.calendar.notifications.service.NotificationsService;
import com.targil.calendar.events.EventConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventConverter converter;
    private final NotificationsService notificationsService;
    private final EventSpecifications specifications;

    @Override
    @Transactional(readOnly = true)
    public ResponseEventsPageDTO findEvents(String location, String sortBy, String order, int pageNumber, int pageSize) {
        log.info("Finding events with location: {}, sortBy: {}, order: {}, pageNumber: {}, pageSize: {}", location, sortBy, order, pageNumber, pageSize);
        Specification<EventEntity> spec = specifications.hasLocation(location);
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<EventEntity> page = eventRepository.findAll(spec, pageable);
        return converter.convertToDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEventDTO findEventById(Long id) {
        log.info("Finding event by ID: {}", id);
        EventEntity event = findEvent(id);
        return converter.convertToDTO(event);
    }

    @Override
    @Transactional
    public ResponseEventDTO createEvent(CreateEventDTO event) {
        log.info("Creating event: {}", event.title());
        EventEntity savedEvent = eventRepository.save(converter.convertToEntity(event));
        log.info("Event created with ID: {}", savedEvent.getId());
        return converter.convertToDTO(savedEvent);
    }

    @Override
    @Transactional
    public ResponseEventDTO updateEvent(Long id, EventEntity eventDetails) {
        log.info("Updating event: {}", id);
        EventEntity event = findEvent(id);
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setLocation(eventDetails.getLocation());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());
        EventEntity updatedEvent = eventRepository.save(event);
        log.info("Event updated with ID: {}", updatedEvent.getId());
        List<String> emails = eventRepository.findSubscriberEmailsByEventId(id);
        notifyUsers(emails, "Event '" + eventDetails.getTitle() + "' was updated");
        return converter.convertToDTO(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        log.info("Deleting event: {}", id);
        EventEntity event = findEvent(id);
        List<String> emails = eventRepository.findSubscriberEmailsByEventId(id);
        notifyUsers(emails, "Event '" + event.getTitle() + "' was deleted");
        eventRepository.delete(event);
        log.info("Event deleted with ID: {}", id);
    }

    private EventEntity findEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> {
            log.error("Event not found with ID: {}", id);
            return new EventNotFoundException("Event with id : " + id + " is not found");
        });
    }

    @Async
    protected void notifyUsers(List<String> emails, String notificationMessage) {
        log.info("Notifying users about event update/deletion");
        EventNotification eventNotification = new EventNotification(this, notificationMessage, emails);
        notificationsService.notifySubscribers(eventNotification);
    }
}
