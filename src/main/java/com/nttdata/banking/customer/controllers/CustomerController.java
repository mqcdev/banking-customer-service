package com.nttdata.banking.customer.controllers;

import com.nttdata.banking.customer.models.Customer;
import com.nttdata.banking.customer.services.CustomerService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping
    public Mono<ResponseEntity<Customer>> createCustomer(@RequestBody Customer customer) {
        log.info("Creating new customer");
        return customerService.save(customer)
                .map(savedCustomer -> ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping
    public Flux<Customer> getAllCustomers() {
        log.info("Getting all customers");
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Customer>> getCustomerById(@PathVariable String id) {
        log.info("Getting customer by ID: {}", id);
        return customerService.findById(id)
                .map(customer -> ResponseEntity.ok().body(customer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> updateCustomer(@PathVariable String id,
                                                         @RequestBody Customer customer) {
        log.info("Updating customer with ID: {}", id);
        customer.setId(id);
        return customerService.update(customer)
                .map(updatedCustomer -> ResponseEntity.ok().body(updatedCustomer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable String id) {
        log.info("Deleting customer with ID: {}", id);
        return customerService.deleteById(id)
                .map(result -> ResponseEntity.noContent().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping("/document/{documentNumber}")
    public Mono<ResponseEntity<Customer>> getCustomerByDocumentNumber(@PathVariable String documentNumber) {
        log.info("Getting customer by document number: {}", documentNumber);
        return customerService.findByDocumentNumber(documentNumber)
                .map(customer -> ResponseEntity.ok().body(customer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/ruc/{ruc}")
    public Mono<ResponseEntity<Customer>> getCustomerByRuc(@PathVariable String ruc) {
        log.info("Getting customer by RUC: {}", ruc);
        return customerService.findByRuc(ruc)
                .map(customer -> ResponseEntity.ok().body(customer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
