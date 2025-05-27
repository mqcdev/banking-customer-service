package com.nttdata.banking.customer.services.impl;

import com.nttdata.banking.customer.dto.request.CustomerRequestDTO;
import com.nttdata.banking.customer.dto.response.CustomerResponseDTO;
import com.nttdata.banking.customer.exception.CustomerNotFoundException;
import com.nttdata.banking.customer.exception.DuplicateCustomerException;
import com.nttdata.banking.customer.mapper.CustomerMapper;
import com.nttdata.banking.customer.repositories.CustomerRepository;
import com.nttdata.banking.customer.services.CustomerService;
import com.nttdata.banking.customer.utils.CustomerValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerValidator customerValidator;

    @Override
    public Mono<CustomerResponseDTO> create(CustomerRequestDTO requestDto) {
        log.info("Creating new customer with email: {}", requestDto.getEmail());

        return Mono.just(requestDto)
                .doOnNext(dto -> {
                    log.debug("Validating customer request");
                    customerValidator.validateCustomerRequest(dto);
                })
                .flatMap(this::validateUniqueFields)
                .map(customerMapper::toEntity)
                .flatMap(customerRepository::save)
                .map(customerMapper::toResponseDto)
                .doOnSuccess(response -> log.info("Customer created successfully with ID: {}", response.getId()))
                .doOnError(error -> log.error("Error creating customer: {}", error.getMessage()));
    }

    @Override
    public Flux<CustomerResponseDTO> findAll() {
        log.info("Retrieving all customers");

        return customerRepository.findAll()
                .map(customerMapper::toResponseDto)
                .doOnComplete(() -> log.info("Retrieved all customers successfully"))
                .doOnError(error -> log.error("Error retrieving customers: {}", error.getMessage()));
    }

    @Override
    public Mono<CustomerResponseDTO> findById(String id) {
        log.info("Retrieving customer by ID: {}", id);

        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ID: " + id)))
                .map(customerMapper::toResponseDto)
                .doOnSuccess(response -> log.info("Customer retrieved successfully: {}", id))
                .doOnError(error -> log.error("Error retrieving customer {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<CustomerResponseDTO> update(String id, CustomerRequestDTO requestDto) {
        log.info("Updating customer with ID: {}", id);

        return Mono.just(requestDto)
                .doOnNext(dto -> {
                    log.debug("Validating customer update request");
                    customerValidator.validateCustomerRequest(dto);
                })
                .flatMap(dto -> validateUniqueFieldsForUpdate(id, dto))
                .flatMap(dto -> customerRepository.findById(id)
                        .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ID: " + id)))
                        .doOnNext(existingCustomer -> customerMapper.updateEntityFromDto(dto, existingCustomer))
                )
                .flatMap(customerRepository::save)
                .map(customerMapper::toResponseDto)
                .doOnSuccess(response -> log.info("Customer updated successfully: {}", id))
                .doOnError(error -> log.error("Error updating customer {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.info("Deleting customer with ID: {}", id);

        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ID: " + id)))
                .flatMap(customer -> customerRepository.deleteById(id))
                .doOnSuccess(unused -> log.info("Customer deleted successfully: {}", id))
                .doOnError(error -> log.error("Error deleting customer {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<CustomerResponseDTO> findByEmail(String email) {
        log.info("Retrieving customer by email: {}", email);

        return customerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with email: " + email)))
                .map(customerMapper::toResponseDto)
                .doOnSuccess(response -> log.info("Customer retrieved by email successfully: {}", email))
                .doOnError(error -> log.error("Error retrieving customer by email {}: {}", email, error.getMessage()));
    }

    @Override
    public Mono<CustomerResponseDTO> findByDocumentNumber(String documentNumber) {
        log.info("Retrieving customer by document number: {}", documentNumber);

        return customerRepository.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with document number: " + documentNumber)))
                .map(customerMapper::toResponseDto)
                .doOnSuccess(response -> log.info("Customer retrieved by document number successfully: {}", documentNumber))
                .doOnError(error -> log.error("Error retrieving customer by document number {}: {}", documentNumber, error.getMessage()));
    }

    @Override
    public Flux<CustomerResponseDTO> findByActive() {
        log.info("Retrieving active customers");

        return customerRepository.findByActiveTrue()
                .map(customerMapper::toResponseDto)
                .doOnComplete(() -> log.info("Retrieved active customers successfully"))
                .doOnError(error -> log.error("Error retrieving active customers: {}", error.getMessage()));
    }


    private Mono<CustomerRequestDTO> validateUniqueFields(CustomerRequestDTO requestDto) {
        return Mono.zip(
                customerRepository.existsByEmail(requestDto.getEmail()),
                customerRepository.existsByDocumentNumber(requestDto.getDocumentNumber()),
                requestDto.getRuc() != null ? customerRepository.existsByRuc(requestDto.getRuc()) : Mono.just(false)
        ).flatMap(tuple -> {
            boolean emailExists = tuple.getT1();
            boolean documentExists = tuple.getT2();
            boolean rucExists = tuple.getT3();

            if (emailExists) {
                return Mono.error(new DuplicateCustomerException("Customer already exists with email: " + requestDto.getEmail()));
            }
            if (documentExists) {
                return Mono.error(new DuplicateCustomerException("Customer already exists with document number: " + requestDto.getDocumentNumber()));
            }
            if (rucExists) {
                return Mono.error(new DuplicateCustomerException("Customer already exists with RUC: " + requestDto.getRuc()));
            }

            return Mono.just(requestDto);
        });
    }


    private Mono<CustomerRequestDTO> validateUniqueFieldsForUpdate(String id, CustomerRequestDTO requestDto) {
        return Mono.zip(
                customerRepository.existsByEmailAndIdNot(requestDto.getEmail(), id),
                customerRepository.existsByDocumentNumberAndIdNot(requestDto.getDocumentNumber(), id),
                requestDto.getRuc() != null ? customerRepository.existsByRucAndIdNot(requestDto.getRuc(), id) : Mono.just(false)
        ).flatMap(tuple -> {
            boolean emailExists = tuple.getT1();
            boolean documentExists = tuple.getT2();
            boolean rucExists = tuple.getT3();

            if (emailExists) {
                return Mono.error(new DuplicateCustomerException("Another customer already exists with email: " + requestDto.getEmail()));
            }
            if (documentExists) {
                return Mono.error(new DuplicateCustomerException("Another customer already exists with document number: " + requestDto.getDocumentNumber()));
            }
            if (rucExists) {
                return Mono.error(new DuplicateCustomerException("Another customer already exists with RUC: " + requestDto.getRuc()));
            }

            return Mono.just(requestDto);
        });
    }
}
