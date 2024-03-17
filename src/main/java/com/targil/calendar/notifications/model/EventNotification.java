package com.targil.calendar.notifications.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class EventNotification extends ApplicationEvent {
    private final String message;
    private final List<String> mails;
    public EventNotification(Object source, String message, List<String> mails) {
        super(source);
        this.message = message;
        this.mails = mails;
    }
}
