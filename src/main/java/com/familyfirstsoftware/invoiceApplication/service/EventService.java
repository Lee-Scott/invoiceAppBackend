package com.familyfirstsoftware.invoiceApplication.service;

import com.familyfirstsoftware.invoiceApplication.domain.UserEvent;
import com.familyfirstsoftware.invoiceApplication.enumeration.EventType;

import java.util.Collection;

public interface EventService {
    Collection<UserEvent> getEventsByUserId(Long userId);
    void addUserEvent(String email, EventType eventType, String device, String ipAddress);
    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);
}
