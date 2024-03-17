package com.targil.calendar.subscriptions.controller;

import com.targil.calendar.subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToEvent(@RequestParam Long userId, @RequestParam Long eventId) {
        boolean subscribed = subscriptionService.subscribeToEvent(userId, eventId);
        return subscribed ?
                ok().body("Subscribed successfully") :
                status(HttpStatus.CONFLICT).body("Already subscribed");
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeFromEvent(@RequestParam Long userId, @RequestParam Long eventId) {
        boolean unsubscribed = subscriptionService.unsubscribeFromEvent(userId, eventId);
        return unsubscribed ?
                ok().body("Unsubscribed successfully") :
                status(HttpStatus.CONFLICT).body("Not subscribed");
    }
}
