package com.familyfirstsoftware.invoiceApplication.service.implementation;

import com.familyfirstsoftware.invoiceApplication.domain.Customer;
import com.familyfirstsoftware.invoiceApplication.domain.Invoice;
import com.familyfirstsoftware.invoiceApplication.domain.Stats;
import com.familyfirstsoftware.invoiceApplication.repository.CustomerRepository;
import com.familyfirstsoftware.invoiceApplication.repository.InvoiceRepository;
import com.familyfirstsoftware.invoiceApplication.rowMapper.StatsRowMapper;
import com.familyfirstsoftware.invoiceApplication.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import static com.familyfirstsoftware.invoiceApplication.query.CustomerQuery.STATS_QUERY;
import static java.util.Map.of;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(new Date());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> getCustomers(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Customer> searchCustomers(String name, int page, int size) {

        return customerRepository.findByNameContaining(name, PageRequest.of(page, size));
    }

    // TODO pull invoice functionalities into their own service
    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceNumber(RandomStringUtils.randomAlphabetic(10).toUpperCase());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Page<Invoice> getInvoices(int page, int size) {
        return invoiceRepository.findAll(PageRequest.of(page, size));
    }


    @Override
    public void addInvoiceToCustomer(Long id, Invoice invoice) {
        invoice.setInvoiceNumber(RandomStringUtils.randomAlphabetic(10).toUpperCase());
        Customer customer = customerRepository.findById(id).get(); // .get() is not used here because it throws an exception if the customer is not found

        invoice.setCustomer(customer);
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id).get();
    }

    @Override
    public Stats getStats() {
        // put his in a repository
        return jdbc.queryForObject(STATS_QUERY, of(), new StatsRowMapper());
    }
}
