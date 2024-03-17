package com.targil.calendar.notifications.service;

import com.targil.calendar.notifications.model.EventNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationsService {
    @Async
    public void notifySubscribers(EventNotification message) {
        List<String> emails = message.getMails();
        for (String email : emails) {
            System.out.println("Sending mail to " + email);
            System.out.println(message.getMessage());
        }
    }
}
