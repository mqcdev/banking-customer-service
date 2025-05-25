package com.nttdata.banking.customer.services.impl;

import com.nttdata.banking.customer.models.Customer;
import com.nttdata.banking.customer.repositories.CustomerRepository;
import com.nttdata.banking.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Mono<Customer> save(Customer customer) {
        log.info("Saving customer with document number: {}", customer.getDocumentNumber());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setActive(true);
        return customerRepository.save(customer)
                .doOnSuccess(savedCustomer ->
                        log.info("Customer saved successfully with ID: {}", savedCustomer.getId()))
                .doOnError(error ->
                        log.error("Error saving customer: {}", error.getMessage()));
    }

    @Override
    public Flux<Customer> findAll() {
        log.info("Finding all customers");
        return customerRepository.findAll()
                .doOnComplete(() -> log.info("Retrieved all customers successfully"))
                .doOnError(error ->
                        log.error("Error retrieving customers: {}", error.getMessage()));
    }

    @Override
    public Mono<Customer> findById(String id) {
        log.info("Finding customer by ID: {}", id);
        return customerRepository.findById(id)
                .doOnSuccess(customer ->
                        log.info("Customer found with ID: {}", id))
                .doOnError(error ->
                        log.error("Error finding customer by ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<Customer> update(Customer customer) {
        log.info("Updating customer with ID: {}", customer.getId());
        return customerRepository.findById(customer.getId())
                .flatMap(existingCustomer -> {
                    customer.setCreatedAt(existingCustomer.getCreatedAt());
                    customer.setUpdatedAt(LocalDateTime.now());
                    return customerRepository.save(customer);
                })
                .doOnSuccess(updatedCustomer ->
                        log.info("Customer updated successfully with ID: {}", updatedCustomer.getId()))
                .doOnError(error ->
                        log.error("Error updating customer: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.info("Deleting customer by ID: {}", id);
        return customerRepository.findById(id)
                .flatMap(customer -> customerRepository.deleteById(id))
                .doOnSuccess(result ->
                        log.info("Customer deleted successfully with ID: {}", id))
                .doOnError(error ->
                        log.error("Error deleting customer by ID {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<Customer> findByDocumentNumber(String documentNumber) {
        log.info("Finding customer by document number: {}", documentNumber);
        return customerRepository.findByDocumentNumber(documentNumber)
                .doOnSuccess(customer ->
                        log.info("Customer found with document number: {}", documentNumber))
                .doOnError(error ->
                        log.error("Error finding customer by document number {}: {}",
                                documentNumber, error.getMessage()));
    }

    @Override
    public Mono<Customer> findByRuc(String ruc) {
        log.info("Finding customer by RUC: {}", ruc);
        return customerRepository.findByRuc(ruc)
                .doOnSuccess(customer ->
                        log.info("Customer found with RUC: {}", ruc))
                .doOnError(error ->
                        log.error("Error finding customer by RUC {}: {}", ruc, error.getMessage()));
    }
}
