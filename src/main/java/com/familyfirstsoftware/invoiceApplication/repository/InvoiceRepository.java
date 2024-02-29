package com.familyfirstsoftware.invoiceApplication.repository;

import com.familyfirstsoftware.invoiceApplication.domain.Customer;
import com.familyfirstsoftware.invoiceApplication.domain.Invoice;
import org.springframework.data.repository.ListCrudRepository;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long>, ListCrudRepository<Invoice, Long> {


}


