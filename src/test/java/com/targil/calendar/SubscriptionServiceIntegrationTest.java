package com.targil.calendar;

import com.targil.calendar.subscriptions.service.SubscriptionService;
import com.targil.calendar.events.dao.EventRepository;
import com.targil.calendar.users.repository.UserRepository;
import com.targil.calendar.events.model.entity.EventEntity;
import com.targil.calendar.users.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SubscriptionServiceIntegrationTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private UserEntity user;
    private EventEntity event;

    @BeforeEach
    public void setup() {
        user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);
        event = EventEntity.builder()
                .title("Test Event")
                .description("This is a test event")
                .location("Test Location")
                .date(LocalDate.now())
                .creationTime(LocalDateTime.now())
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .subscribers(new HashSet<>())
                .build();
        event = eventRepository.save(event);
    }

    @Test
    public void testSubscribeAndUnsubscribeEvent() {
        boolean subscribed = subscriptionService.subscribeToEvent(user.getId(), event.getId());
        assertThat(subscribed).isTrue();

        UserEntity updatedUser = userRepository.findById(user.getId()).get();
        assertThat(updatedUser.getSubscribedEvents()).contains(event);

        boolean unsubscribed = subscriptionService.unsubscribeFromEvent(user.getId(), event.getId());
        assertThat(unsubscribed).isTrue();

        updatedUser = userRepository.findById(user.getId()).get();
        assertThat(updatedUser.getSubscribedEvents()).doesNotContain(event);
    }
}
