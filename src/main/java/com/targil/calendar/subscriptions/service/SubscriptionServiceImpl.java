package com.targil.calendar.subscriptions.service;

import com.targil.calendar.events.dao.EventRepository;
import com.targil.calendar.users.repository.UserRepository;
import com.targil.calendar.events.model.entity.EventEntity;
import com.targil.calendar.users.model.entity.UserEntity;
import com.targil.calendar.events.model.exception.EventNotFoundException;
import com.targil.calendar.users.model.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private static final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    /*
     This method may trigger the loading of the entire collection of subscribed events due to the use of .contains(), etc.
     Consider optimizing this by using a JPQL query to check for the existence of the subscription directly in the database,
     which can avoid loading the collection and improve performance.
     */
    @Transactional
    @Override
    public boolean subscribeToEvent(Long userId, Long eventId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        EventEntity event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));
        if (!eventRepository.isUserSubscribedToEvent(eventId, userId)) {
            user.getSubscribedEvents().add(event);
            event.getSubscribers().add(user);
            log.info("User {} subscribed to event {}", userId, eventId);
            return true;
        } else {
            log.info("User {} is already subscribed to event {}", userId, eventId);
            return false;
        }
    }

    @Transactional
    @Override
    public boolean unsubscribeFromEvent(Long userId, Long eventId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User " + userId + " is not found"));
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event " + eventId + " is not found"));
        if (eventRepository.isUserSubscribedToEvent(eventId, userId)) {
            user.getSubscribedEvents().remove(event);
            log.info("User {} unsubscribed from event {}", userId, eventId);
            return true;
        } else {
            log.info("User {} is not subscribed to event {}", userId, eventId);
            return false;
        }
    }
}
