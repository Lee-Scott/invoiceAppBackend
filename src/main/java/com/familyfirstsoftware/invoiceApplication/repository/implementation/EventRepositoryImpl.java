package com.familyfirstsoftware.invoiceApplication.repository.implementation;


import com.familyfirstsoftware.invoiceApplication.domain.UserEvent;
import com.familyfirstsoftware.invoiceApplication.enumeration.EventType;
import com.familyfirstsoftware.invoiceApplication.repository.EventRepository;
import com.familyfirstsoftware.invoiceApplication.rowMapper.UserEventRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;


import static com.familyfirstsoftware.invoiceApplication.query.EventQuery.*;
import static java.util.Map.of;


@Repository
@RequiredArgsConstructor
@Slf4j
public class EventRepositoryImpl implements EventRepository {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Collection<UserEvent> getEventsByUserId(Long userId) {
        return jdbc.query(SELECT_EVENTS_BY_USER_ID_QUERY, of("id", userId), new UserEventRowMapper());
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        //System.out.println("***email: " + email + " eventType: " + eventType + " device: " + device + " ipAddress: " + ipAddress);
        jdbc.update(INSERT_EVENT_BY_USER_EMAIL_QUERY, of("email", email, "type", eventType.toString(), "device", device, "ipAddress", ipAddress));
    }

    // TODO - this method is not used, it needs testing
    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {
        //System.out.println("userId: " + userId + " eventType: " + eventType + " device: " + device + " ipAddress: " + ipAddress);
        jdbc.update(INSERT_EVENT_BY_USER_ID_QUERY, of("userId", userId, "type", eventType.toString(), "device", device, "ipAddress", ipAddress));

    }
}
