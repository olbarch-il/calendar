package com.targil.calendar.events.dao;

import com.targil.calendar.events.model.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {
    @Query("SELECT e FROM EventEntity e WHERE e.startTime BETWEEN ?1 AND ?2")
    List<EventEntity> findEventsBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT u.email FROM EventEntity e JOIN e.subscribers u WHERE e.id = :eventId")
    List<String> findSubscriberEmailsByEventId(@Param("eventId") Long eventId);

    @Query("SELECT COUNT(sub) > 0 FROM EventEntity e JOIN e.subscribers sub WHERE e.id = :eventId AND sub.id = :userId")
    boolean isUserSubscribedToEvent(@Param("eventId") Long eventId, @Param("userId") Long userId);
}
