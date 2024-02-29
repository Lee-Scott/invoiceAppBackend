package com.familyfirstsoftware.invoiceApplication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String invoiceNumber;
    private String services; // todo should be its own entity
    private Date date;
    private String status;
    private double total;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false) // Create a foreign key in the invoice table for customer_id, nullable is false because every invoice must have a customer
    @JsonIgnore // This is to prevent infinite recursion when serializing the object. We have customer in this class and invoices in the Customer class
    private Customer customer;
}
