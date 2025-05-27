package com.nttdata.banking.customer.services;

import com.nttdata.banking.customer.dto.request.CustomerRequestDTO;
import com.nttdata.banking.customer.dto.response.CustomerResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerResponseDTO> create(CustomerRequestDTO requestDto);

    Flux<CustomerResponseDTO> findAll();

    Mono<CustomerResponseDTO> findById(String id);

    Mono<CustomerResponseDTO> update(String id, CustomerRequestDTO requestDto);

    Mono<Void> deleteById(String id);

    Mono<CustomerResponseDTO> findByEmail(String email);

    Mono<CustomerResponseDTO> findByDocumentNumber(String documentNumber);

    Flux<CustomerResponseDTO> findByActive();
}

