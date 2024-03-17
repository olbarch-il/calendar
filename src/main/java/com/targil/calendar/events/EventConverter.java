package com.targil.calendar.events;

import com.targil.calendar.events.model.dto.*;
import com.targil.calendar.events.model.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventConverter {

    public ResponseEventDTO convertToDTO(EventEntity event) {
        return ResponseEventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .endTime(event.getEndTime())
                .startTime(event.getStartTime())
                .location(event.getLocation())
                .description(event.getDescription())
                .popularity(event.getSubscribersCount())
                .build();
    }

    public ResponseEventsPageDTO convertToDTO(Page<EventEntity> page) {
        return ResponseEventsPageDTO.builder()
                .events(page.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .currentPage(page.getPageable().getPageNumber())
                .pageSize(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public EventEntity convertToEntity(CreateEventDTO event) {
        return EventEntity.builder()
                .title(event.title())
                .location(event.location())
                .startTime(event.startTime())
                .endTime(event.endTime())
                .description(event.description())
                .date(event.startTime().toLocalDate())
                .creationTime(LocalDateTime.now())
                .build();
    }

    public List<EventEntity> convertToEntity(BatchCreateEventsDTO batch) {
        return batch.events().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

}
