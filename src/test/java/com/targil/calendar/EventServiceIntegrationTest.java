package com.targil.calendar;

import com.targil.calendar.events.model.dto.CreateEventDTO;
import com.targil.calendar.events.model.dto.ResponseEventDTO;
import com.targil.calendar.events.model.dto.ResponseEventsPageDTO;
import com.targil.calendar.events.model.entity.EventEntity;
import com.targil.calendar.events.model.exception.EventNotFoundException;
import com.targil.calendar.events.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class EventServiceIntegrationTest {

    @Autowired
    private EventService eventService;
    @Test
    public void testCreateEvent() {
        CreateEventDTO createEventDTO = new CreateEventDTO("Test Event", "This is a test event", "Test Location", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        ResponseEventDTO createdEvent = eventService.createEvent(createEventDTO);
        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.title()).isEqualTo(createEventDTO.title());
        assertThat(createdEvent.description()).isEqualTo(createEventDTO.description());
        assertThat(createdEvent.location()).isEqualTo(createEventDTO.location());
        assertThat(createdEvent.startTime()).isEqualTo(createEventDTO.startTime());
        assertThat(createdEvent.endTime()).isEqualTo(createEventDTO.endTime());
        ResponseEventDTO persistedEvent = eventService.findEventById(createdEvent.id());
        assertThat(persistedEvent).isNotNull();
        assertThat(persistedEvent.title()).isEqualTo(createEventDTO.title());
    }

    private ResponseEventDTO createAndSaveEvent(String title, String loc, LocalDateTime localDateTime) {
        CreateEventDTO createEventDTO = new CreateEventDTO(title, "This is a test event",
                loc, localDateTime.plusHours(1), localDateTime.plusHours(2));
        return eventService.createEvent(createEventDTO);
    }

    @Test
    public void testUpdateEvent() {
        ResponseEventDTO createdEvent = createAndSaveEvent("Event 1", "Location 1", LocalDateTime.now().plusDays(1));
        EventEntity eventToUpdate = new EventEntity();
        eventToUpdate.setId(createdEvent.id());
        eventToUpdate.setTitle("Updated Event");
        eventToUpdate.setDescription("Updated description");
        eventToUpdate.setLocation("Updated Location");
        eventToUpdate.setStartTime(LocalDateTime.now().plusDays(1));
        eventToUpdate.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        ResponseEventDTO updatedEvent = eventService.updateEvent(createdEvent.id(), eventToUpdate);
        assertThat(updatedEvent.title()).isEqualTo("Updated Event");
        assertThat(updatedEvent.description()).isEqualTo("Updated description");
        assertThat(updatedEvent.location()).isEqualTo("Updated Location");
        ResponseEventDTO eventById = eventService.findEventById(createdEvent.id());
        assertThat(eventById.title()).isEqualTo("Updated Event");
        assertThat(eventById.description()).isEqualTo("Updated description");
        assertThat(eventById.location()).isEqualTo("Updated Location");
    }

    @Test
    public void testFindEventById() {
        ResponseEventDTO createdEvent = createAndSaveEvent("Event for ID search", "Specific Location", LocalDateTime.now().plusDays(1));
        ResponseEventDTO foundEvent = eventService.findEventById(createdEvent.id());
        assertThat(foundEvent.id()).isEqualTo(createdEvent.id());
    }

    @Test
    public void testFindEventsByLocation() {
        createAndSaveEvent("Event 1", "Location A", LocalDateTime.now().plusDays(1));
        createAndSaveEvent("Event 2", "Location B", LocalDateTime.now().plusDays(1));
        ResponseEventsPageDTO eventsInLocationA = eventService.findEvents("Location A", "title", "asc", 0, 10);
        ResponseEventsPageDTO eventsInLocationB = eventService.findEvents("Location B", "title", "asc", 0, 10);
        assertThat(eventsInLocationA.events()).hasSize(1);
        assertThat(eventsInLocationB.events()).hasSize(1);
        assertThat(eventsInLocationA.events().get(0).location()).isEqualTo("Location A");
        assertThat(eventsInLocationB.events().get(0).location()).isEqualTo("Location B");
    }

    @Test
    public void testDeleteEventAndCheckSubscribers() {
        ResponseEventDTO createdEvent = createAndSaveEvent("Event to be deleted", "Deletion Location", LocalDateTime.now().plusDays(1));
        eventService.deleteEvent(createdEvent.id());
        Exception exception = assertThrows(EventNotFoundException.class, () -> {
            eventService.findEventById(createdEvent.id());
        });
        assertThat(exception.getMessage()).contains("Event with id : " + createdEvent.id() + " is not found");
    }

    @Test
    public void testFindEventsWithFilterAndSort() {
        createAndSaveEvent("Event 1", "Location 1", LocalDateTime.now().plusDays(1));
        createAndSaveEvent("Event 2", "Location 2!", LocalDateTime.now().plusDays(2));
        ResponseEventsPageDTO eventsPage = eventService.findEvents("Location 1", "startTime", "asc", 0, 10);
        assertThat(eventsPage.events()).hasSize(1);
        assertThat(eventsPage.events().get(0).title()).isEqualTo("Event 1");
    }


}
