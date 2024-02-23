package com.familyfirstsoftware.invoiceApplication.service.implementation;

import com.familyfirstsoftware.invoiceApplication.domain.UserEvent;
import com.familyfirstsoftware.invoiceApplication.enumeration.EventType;
import com.familyfirstsoftware.invoiceApplication.repository.EventRepository;
import com.familyfirstsoftware.invoiceApplication.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    @Override
    public Collection<UserEvent> getEventsByUserId(Long userId) {
        return eventRepository.getEventsByUserId(userId);
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        //System.out.println("***email: " + email + " eventType: " + eventType + " device: " + device + " ipAddress: " + ipAddress);
        eventRepository.addUserEvent(email, eventType, device, ipAddress);
    }

    // TODO - this method is not used, it needs testing
    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {
        //System.out.println("***userId: " + userId + " eventType: " + eventType + " device: " + device + " ipAddress: " + ipAddress);
        eventRepository.addUserEvent(userId, eventType, device, ipAddress);
    }
}
