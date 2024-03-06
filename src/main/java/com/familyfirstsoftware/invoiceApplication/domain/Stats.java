package com.familyfirstsoftware.invoiceApplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(NON_DEFAULT) // when jackson does the json mapping, it will ignore the default values like 0.00 for total billed
public class Stats {
    //TODO - billed by type
    private int totalCustomers;
    private int totalInvoices;
    private double totalBilled;

}
