package com.targil.calendar.events.controller;

import com.targil.calendar.events.model.dto.BatchCreateEventsDTO;
import com.targil.calendar.events.model.dto.BatchUpdateEventsDTO;
import com.targil.calendar.events.service.BatchEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/events/batch")
@RequiredArgsConstructor
public class BatchEventController {

    private final BatchEventService eventService;

    @PostMapping
    public CompletableFuture<ResponseEntity<String>> createEventsBatch(@RequestBody BatchCreateEventsDTO events) {
        return eventService.createEvents(events)
                .thenApply(response -> ResponseEntity.status(HttpStatus.CREATED).body("Success. Ids: " + response));
    }

    @PutMapping
    public CompletableFuture<ResponseEntity<String>> updateEventsBatch(@RequestBody BatchUpdateEventsDTO events) {
        CompletableFuture<Integer> updatedCountFuture = eventService.updateEvents(events);
        return updatedCountFuture.thenApply(updatedCount ->
                ResponseEntity.ok().body("Updated " + updatedCount + " events")
        );
    }

    @DeleteMapping
    public CompletableFuture<ResponseEntity<String>> deleteEventsBatch(@RequestBody List<Long> eventIds) {
        return eventService.deleteEvents(eventIds).thenApply(updatedCount ->
                ResponseEntity.ok().body("Updated " + updatedCount + " events")
        );
    }
}
