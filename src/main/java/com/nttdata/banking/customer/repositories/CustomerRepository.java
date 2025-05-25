package com.nttdata.banking.customer.repositories;

import com.nttdata.banking.customer.models.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Mono<Customer> findByDocumentNumber(String documentNumber);

    Mono<Customer> findByRuc(String ruc);

    Flux<Customer> findByActive(Boolean active);
}
