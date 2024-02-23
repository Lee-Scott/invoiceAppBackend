package com.familyfirstsoftware.invoiceApplication.listener;


import com.familyfirstsoftware.invoiceApplication.event.NewUserEvent;
import com.familyfirstsoftware.invoiceApplication.service.EventService;
import com.familyfirstsoftware.invoiceApplication.utils.RequestEventUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;




@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserEventListener {
    private final EventService eventService;
    private final HttpServletRequest request;

    // the RequestEventUtils parser takes forever to parse the request
    @EventListener
    public void onNewUserEvent(NewUserEvent event) {
        //log.info("Received new user event: {}", event);
        eventService.addUserEvent(
                event.getEmail(),
                event.getType(),
                RequestEventUtils.getDevice(request),
                RequestEventUtils.getIpAddress(request));
    }
}
