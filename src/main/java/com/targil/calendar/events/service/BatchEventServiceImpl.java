package com.targil.calendar.events.service;

import com.targil.calendar.events.dao.EventRepository;
import com.targil.calendar.events.model.dto.BatchCreateEventsDTO;
import com.targil.calendar.events.model.dto.BatchUpdateEventsDTO;
import com.targil.calendar.events.model.entity.EventEntity;
import com.targil.calendar.events.EventConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BatchEventServiceImpl implements BatchEventService {
    private final EventRepository eventRepository;
    private final EventConverter converter;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Async
    @Transactional
    public CompletableFuture<List<Long>> createEvents(BatchCreateEventsDTO events) {
        log.info("Creating batch events");
        List<EventEntity> entitiesForCreating = converter.convertToEntity(events);
        List<EventEntity> entities = eventRepository.saveAll(entitiesForCreating);
        List<Long> ids = entities.stream().map(EventEntity::getId).collect(Collectors.toList());
        log.info("Batch events created with IDs: {}", ids);
        return CompletableFuture.completedFuture(ids);
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<Integer> updateEvents(BatchUpdateEventsDTO eventsDTO) {
        log.info("Updating batch events");
        String sql = "UPDATE event_entity SET title = ?, description = ?, location = ?, date = ?, start_time = ?, end_time = ? WHERE id = ?";
        List<Object[]> batchArgs = eventsDTO.events().stream().map(dto -> new Object[]{
                dto.title(),
                dto.description(),
                dto.location(),
                dto.startTime() == null ? null : java.sql.Date.valueOf(dto.startTime().toLocalDate()),
                dto.startTime() == null ? null : java.sql.Timestamp.valueOf(dto.startTime()),
                dto.endTime() == null ? null : java.sql.Timestamp.valueOf(dto.endTime()),
                dto.id()
        }).collect(Collectors.toList());
        int[] updateCounts = jdbcTemplate.batchUpdate(sql, batchArgs);
        int updatedCount = Arrays.stream(updateCounts).sum();
        log.info("Batch events updated, total updated count: {}", updatedCount);
        return CompletableFuture.completedFuture(updatedCount);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteEvents(List<Long> eventIds) {
        log.info("Deleting batch events with IDs: {}", eventIds);
        return CompletableFuture.runAsync(() -> {
            eventRepository.deleteAllByIdInBatch(eventIds);
            log.info("Batch events deleted");
        });
    }
}
