package com.targil.calendar.events.model.dto;

import lombok.*;

import java.util.List;



@Builder
public record ResponseEventsPageDTO(List<ResponseEventDTO> events, int currentPage, int totalPages, long totalItems,int pageSize ) {
}
