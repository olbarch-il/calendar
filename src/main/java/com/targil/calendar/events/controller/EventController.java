package com.targil.calendar.events.controller;

import com.targil.calendar.events.model.dto.CreateEventDTO;
import com.targil.calendar.events.model.dto.ResponseEventDTO;
import com.targil.calendar.events.model.dto.ResponseEventsPageDTO;
import com.targil.calendar.events.model.entity.EventEntity;
import com.targil.calendar.events.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@EnableAsync
public class EventController {

    private final EventService eventService;
    @GetMapping
    public ResponseEntity<ResponseEventsPageDTO> getEvents(
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "creationTime") String sortBy,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "asc") String order) {
        ResponseEventsPageDTO eventsPage = eventService.findEvents(location, sortBy, order, pageNumber, pageSize);
        return new ResponseEntity<>(eventsPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEventDTO> getEventById(@PathVariable Long id) {
        return new ResponseEntity<>(eventService.findEventById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody CreateEventDTO event) {
        ResponseEventDTO createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>("Event with id " + createdEvent.id() + " successfully created!", HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseEventDTO> updateEvent(@PathVariable Long id, @RequestBody EventEntity event) {
        ResponseEventDTO updatedEvent = eventService.updateEvent(id, event);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>("Event was successfully deleted!", HttpStatus.OK);
    }

}
