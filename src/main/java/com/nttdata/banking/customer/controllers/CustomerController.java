package com.nttdata.banking.customer.controllers;

import com.nttdata.banking.customer.dto.request.CustomerRequestDTO;
import com.nttdata.banking.customer.dto.response.CustomerResponseDTO;
import com.nttdata.banking.customer.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public Mono<ResponseEntity<CustomerResponseDTO>> addCustomer(@Valid @RequestBody CustomerRequestDTO requestDto) {
        log.info("POST /api/customers - Creating new customer");

        return customerService.create(requestDto)
                .map(customer -> ResponseEntity.status(HttpStatus.CREATED).body(customer))
                .doOnSuccess(response -> log.info("Customer created successfully"))
                .doOnError(error -> log.error("Error creating customer: {}", error.getMessage()));
    }

    @GetMapping
    public Flux<CustomerResponseDTO> getAll(@RequestParam(value = "active", required = false) Boolean activeOnly) {
        log.info("GET /api/customers - Retrieving customers, activeOnly: {}", activeOnly);

        if (Boolean.TRUE.equals(activeOnly)) {
            return customerService.findByActive()
                    .doOnComplete(() -> log.info("Retrieved active customers successfully"));
        }

        return customerService.findAll()
                .doOnComplete(() -> log.info("Retrieved all customers successfully"));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomer(@PathVariable String id) {
        log.info("GET /api/customers/{} - Retrieving customer by ID", id);

        return customerService.findById(id)
                .map(customer -> ResponseEntity.ok(customer))
                .doOnSuccess(response -> log.info("Customer retrieved successfully: {}", id))
                .doOnError(error -> log.error("Error retrieving customer {}: {}", id, error.getMessage()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponseDTO>> updateCustomer(@PathVariable String id, @Valid @RequestBody CustomerRequestDTO requestDto) {
        log.info("PUT /api/customers/{} - Updating customer", id);

        return customerService.update(id, requestDto)
                .map(customer -> ResponseEntity.ok(customer))
                .doOnSuccess(response -> log.info("Customer updated successfully: {}", id))
                .doOnError(error -> log.error("Error updating customer {}: {}", id, error.getMessage()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable String id) {
        log.info("DELETE /api/customers/{} - Deleting customer", id);

        return customerService.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .doOnSuccess(response -> log.info("Customer deleted successfully: {}", id))
                .doOnError(error -> log.error("Error deleting customer {}: {}", id, error.getMessage()));
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomerByEmail(@PathVariable String email) {
        log.info("GET /api/customers/email/{} - Retrieving customer by email", email);

        return customerService.findByEmail(email)
                .map(customer -> ResponseEntity.ok(customer))
                .doOnSuccess(response -> log.info("Customer retrieved by email successfully: {}", email))
                .doOnError(error -> log.error("Error retrieving customer by email {}: {}", email, error.getMessage()));
    }

    @GetMapping("/document/{documentNumber}")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomerByDocumentNumber(@PathVariable String documentNumber) {
        log.info("GET /api/customers/document/{} - Retrieving customer by document number", documentNumber);

        return customerService.findByDocumentNumber(documentNumber)
                .map(customer -> ResponseEntity.ok(customer))
                .doOnSuccess(response -> log.info("Customer retrieved by document number successfully: {}", documentNumber))
                .doOnError(error -> log.error("Error retrieving customer by document number {}: {}", documentNumber, error.getMessage()));
    }
}

