package com.targil.calendar.events.model.dto;


import java.util.List;

public record BatchUpdateEventsDTO (List<UpdateEventDTO> events) {
}
