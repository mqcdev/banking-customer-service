package com.nttdata.banking.customer.services;

import com.nttdata.banking.customer.models.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<Customer> save(Customer customer);

    Flux<Customer> findAll();

    Mono<Customer> findById(String id);

    Mono<Customer> update(Customer customer);

    Mono<Void> deleteById(String id);

    Mono<Customer> findByDocumentNumber(String documentNumber);

    Mono<Customer> findByRuc(String ruc);
}

