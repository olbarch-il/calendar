package com.targil.calendar.events.model.dto;

import java.util.List;

public record BatchCreateEventsDTO (List<CreateEventDTO> events) {}


