package com.targil.calendar.events.service;

import com.targil.calendar.events.model.dto.BatchCreateEventsDTO;
import com.targil.calendar.events.model.dto.BatchUpdateEventsDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BatchEventService {
    CompletableFuture<List<Long>> createEvents(BatchCreateEventsDTO events);
    CompletableFuture<Integer> updateEvents(BatchUpdateEventsDTO events);
    CompletableFuture<Void> deleteEvents(List<Long> eventIds);
}
