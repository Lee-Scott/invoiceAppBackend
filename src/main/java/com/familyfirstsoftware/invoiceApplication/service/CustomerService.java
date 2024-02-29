package com.familyfirstsoftware.invoiceApplication.service;

import com.familyfirstsoftware.invoiceApplication.domain.Customer;
import com.familyfirstsoftware.invoiceApplication.domain.Invoice;
import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import org.springframework.data.domain.Page;

public interface CustomerService {

    // Customer functionalities
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Page<Customer> getCustomers(int page, int size);
    Iterable<Customer> getCustomers();
    Customer getCustomerById(Long id);
    Page<Customer> searchCustomers(String name, int page, int size);

    // Invoice functionalities 
    Invoice createInvoice(Invoice invoice);
    Page<Invoice> getInvoices(int page, int size);
    void addInvoiceToCustomer(Long id, Invoice invoiceId);

    Invoice getInvoice(Long id);


}
