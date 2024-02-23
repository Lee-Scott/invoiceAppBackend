package com.familyfirstsoftware.invoiceApplication.repository;

import com.familyfirstsoftware.invoiceApplication.domain.UserEvent;
import com.familyfirstsoftware.invoiceApplication.enumeration.EventType;
import com.familyfirstsoftware.invoiceApplication.event.Event;

import java.util.Collection;

public interface EventRepository<T extends Event> {
    Collection<UserEvent> getEventsByUserId(Long userId);
    void addUserEvent(String email, EventType eventType, String device, String ipAddress);
    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);

    void reportEvent(T event);
}
