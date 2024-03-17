package com.targil.calendar.events.model.dto;



import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateEventDTO(String title, String description, String location, LocalDateTime startTime, LocalDateTime endTime) { }
