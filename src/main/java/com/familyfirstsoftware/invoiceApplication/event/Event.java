package com.familyfirstsoftware.invoiceApplication.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Event {
    private Long id;
    private String type;
    private String description;
    private String device;
    private String ipAddress;
    private Date createdAt;



}