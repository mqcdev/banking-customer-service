package com.nttdata.banking.customer.repositories;

import com.nttdata.banking.customer.models.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Mono<Customer> findByEmail(String email);

    Mono<Customer> findByDocumentNumber(String documentNumber);

    Mono<Customer> findByRuc(String ruc);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByDocumentNumber(String documentNumber);

    Mono<Boolean> existsByRuc(String ruc);

    Flux<Customer> findByActiveTrue();

    Flux<Customer> findByActiveTrueAndCustomerTypeContaining(String customerType);

    Mono<Boolean> existsByEmailAndIdNot(String email, String id);

    Mono<Boolean> existsByDocumentNumberAndIdNot(String documentNumber, String id);

    Mono<Boolean> existsByRucAndIdNot(String ruc, String id);
}

