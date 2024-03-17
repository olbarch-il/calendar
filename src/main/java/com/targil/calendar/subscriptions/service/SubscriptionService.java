package com.targil.calendar.subscriptions.service;

public interface SubscriptionService {
    boolean subscribeToEvent(Long userId, Long eventId);
    boolean unsubscribeFromEvent(Long userId, Long eventId);
}
