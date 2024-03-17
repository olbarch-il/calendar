package com.targil.calendar.events.model.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
@AllArgsConstructor
public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String s) {
        super(s);
    }
}
