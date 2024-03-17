package com.targil.calendar.notifications.service;

import com.targil.calendar.events.dao.EventRepository;
import com.targil.calendar.events.model.entity.EventEntity;
import com.targil.calendar.notifications.model.EventNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSchedulerService {
    private final EventRepository eventRepository;
    private final NotificationsService notificationsService;
    @Scheduled(fixedRate = 300000)
    public void notifySubscribersOfUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inThirtyMinutes = now.plusMinutes(30);
        List<EventEntity> upcomingEvents = eventRepository.findEventsBetween(now, inThirtyMinutes);

        for (EventEntity event : upcomingEvents) {
            String message = "Reminder: Event '" + event.getTitle() + "' starts in 30 minutes!";
            List<String> emails = eventRepository.findSubscriberEmailsByEventId(event.getId());
            notificationsService.notifySubscribers(new EventNotification(this, message, emails));
        }
    }
}
