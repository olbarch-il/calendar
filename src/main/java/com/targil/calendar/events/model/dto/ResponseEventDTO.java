package com.targil.calendar.events.model.dto;

import lombok.*;

import java.time.LocalDateTime;


@Builder
public record ResponseEventDTO (Long id, String title, String description, String location,long popularity, LocalDateTime startTime, LocalDateTime endTime ) {
}
