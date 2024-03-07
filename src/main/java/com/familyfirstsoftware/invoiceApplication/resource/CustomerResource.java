package com.familyfirstsoftware.invoiceApplication.resource;

import com.familyfirstsoftware.invoiceApplication.domain.Customer;
import com.familyfirstsoftware.invoiceApplication.domain.HttpResponse;

import com.familyfirstsoftware.invoiceApplication.domain.Invoice;
import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import com.familyfirstsoftware.invoiceApplication.service.CustomerService;
import com.familyfirstsoftware.invoiceApplication.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;

@Slf4j
@RestController
@RequestMapping(path = "/customer")
@RequiredArgsConstructor
public class CustomerResource {
    private final CustomerService customerService;
    private final UserService userService;

    // Instead of passing in the Authentication and casting in User Utils, we can use the @AuthenticationPrincipal annotation and not do the casting
    @GetMapping(path = "/list")                                               // types we are expecting to have when we call getPricipal on the Authentication object
    public ResponseEntity<HttpResponse> getCustomers(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", customerService.getCustomers(page.orElse(0), size.orElse(10)), // todo do the oreElse in the service
                                    "stats", customerService.getStats()))
                        .message("Customers retrieved successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
    @PostMapping(path = "/create")
    public ResponseEntity<HttpResponse> createCustomer(@AuthenticationPrincipal UserDTO user, @RequestBody Customer customer) {

        return ResponseEntity.created(URI.create(""))
                .body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.createCustomer(customer)))
                        .message("Customer created successfully")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build());
    }
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<HttpResponse> getCustomer(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.getCustomerById(id)))
                        .message("Customer retrieved successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping(path = "/search")
    public ResponseEntity<HttpResponse> searchCustomer(@AuthenticationPrincipal UserDTO user, Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", customerService.searchCustomers(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Customers retrieved successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCustomer(@AuthenticationPrincipal UserDTO user, @RequestBody Customer customer) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.updateCustomer(customer)))
                        .message("Customer updated")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping(path = "/invoice/create")
    public ResponseEntity<HttpResponse> createInvoice(@AuthenticationPrincipal UserDTO user, @RequestBody Invoice invoice) {
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", userService.getUserByEmail(user.getEmail()),
                                        "invoice", customerService.createInvoice(invoice)))
                                .message("Invoice created successfully")
                                .status(HttpStatus.CREATED)
                                .statusCode(HttpStatus.CREATED.value())
                                .build());
    }

    @GetMapping(path = "/invoice/new")
    public ResponseEntity<HttpResponse> newInvoice(@AuthenticationPrincipal UserDTO user) {
        return ResponseEntity.ok(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", userService.getUserByEmail(user.getEmail()),
                                        "customers", customerService.getCustomers())) // so we can call this endpoint to get the customers and then create an invoice
                                .message("Invoice ready to be added")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build());
    }

    @GetMapping(path = "/invoice/list")
    public ResponseEntity<HttpResponse> getInvoices(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.getInvoices(page.orElse(0), size.orElse(10))))
                        .message("Invoices retrieved successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping(path = "/invoice/get/{id}")
    public ResponseEntity<HttpResponse> getInvoice(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", customerService.getInvoice(id)))
                        .message("Invoices retrieved successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping(path = "/invoice/addtocustomer/{id}")
    public ResponseEntity<HttpResponse> addInvoiceToCustomer(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Invoice invoice) {
        customerService.addInvoiceToCustomer(id, invoice);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.getCustomers()))
                        .message(String.format("Invoice added to customer with ID: %s", id))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

}
