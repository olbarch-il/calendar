package com.targil.calendar;

import com.targil.calendar.events.dao.EventRepository;
import com.targil.calendar.events.model.dto.BatchCreateEventsDTO;
import com.targil.calendar.events.model.dto.BatchUpdateEventsDTO;
import com.targil.calendar.events.model.dto.CreateEventDTO;
import com.targil.calendar.events.model.dto.UpdateEventDTO;
import com.targil.calendar.events.model.entity.EventEntity;
import com.targil.calendar.events.service.BatchEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BatchEventServiceIntegrationTest {

    @Autowired
    private BatchEventService batchEventService;
    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    public void cleanUp() {
        eventRepository.deleteAll();
    }

    @Test
    public void testCreateEventsBatch() throws Exception {
        BatchCreateEventsDTO batchCreateEventsDTO = new BatchCreateEventsDTO(List.of(
                CreateEventDTO.builder()
                        .title("event 1")
                        .description("dec 1")
                        .location("loc 1")
                        .startTime(LocalDateTime.now().plusDays(1))
                        .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                        .build(),
                CreateEventDTO.builder()
                        .title("event 2")
                        .description("dec 2")
                        .location("loc 2")
                        .startTime(LocalDateTime.now().plusDays(2))
                        .endTime(LocalDateTime.now().plusDays(2).plusHours(2))
                        .build()
        ));

        CompletableFuture<List<Long>> futureIds = batchEventService.createEvents(batchCreateEventsDTO);
        List<Long> ids = futureIds.get();
        assertThat(ids).hasSize(2);
    }

    @Test
    public void testDeleteEventsBatch() throws Exception {
        BatchCreateEventsDTO createDto = new BatchCreateEventsDTO(List.of(
                new CreateEventDTO("Event 1", "Description 1", "Location 1", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2)),
                new CreateEventDTO("Event 2", "Description 2", "Location 2", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2))
        ));
        CompletableFuture<List<Long>> createdIdsFuture = batchEventService.createEvents(createDto);
        List<Long> createdIds = createdIdsFuture.get();
        batchEventService.deleteEvents(createdIds).get();
        List<EventEntity> eventsAfterDeletion = eventRepository.findAllById(createdIds);
        assertThat(eventsAfterDeletion).isEmpty();
    }

    @Test
    public void testUpdateEventsBatchWithBuilders() throws Exception {
        BatchCreateEventsDTO createDto = new BatchCreateEventsDTO(List.of(
                CreateEventDTO.builder()
                        .title("Event 1")
                        .description("Description 1")
                        .location("Location 1")
                        .startTime(LocalDateTime.now().plusDays(1))
                        .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                        .build(),
                CreateEventDTO.builder()
                        .title("Event 2")
                        .description("Description 2")
                        .location("Location 2")
                        .startTime(LocalDateTime.now().plusDays(2))
                        .endTime(LocalDateTime.now().plusDays(2).plusHours(2))
                        .build()
        ));
        CompletableFuture<List<Long>> createdIdsFuture = batchEventService.createEvents(createDto);
        List<Long> createdIds = createdIdsFuture.get();
        List<UpdateEventDTO> updateDtos = createdIds.stream().map(id ->
                UpdateEventDTO.builder()
                        .id(id)
                        .title("Updated Title")
                        .description("Updated Description")
                        .location("Updated Location")
                        .startTime(LocalDateTime.now().plusDays(3))
                        .endTime(LocalDateTime.now().plusDays(3).plusHours(2))
                        .build()
        ).collect(Collectors.toList());
        BatchUpdateEventsDTO updateDto = new BatchUpdateEventsDTO(updateDtos);
        CompletableFuture<Integer> updateCountFuture = batchEventService.updateEvents(updateDto);
        Integer updateCount = updateCountFuture.get();
        assertThat(updateCount).isEqualTo(updateDtos.size());
    }



}
