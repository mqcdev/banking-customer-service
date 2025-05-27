package com.nttdata.banking.customer.exception;

import com.nttdata.banking.customer.dto.response.ErrorResponseDTO;
import com.nttdata.banking.customer.dto.response.ValidationErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleCustomerNotFound(CustomerNotFoundException ex) {
        log.warn("Customer not found: {}", ex.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Customer Not Found")
                .message(ex.getMessage())
                .path("/api/v1/customers")
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(DuplicateCustomerException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleDuplicateCustomer(DuplicateCustomerException ex) {
        log.warn("Duplicate customer error: {}", ex.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Duplicate Customer")
                .message(ex.getMessage())
                .path("/api/v1/customers")
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }

    @ExceptionHandler(InvalidDocumentException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleInvalidDocument(InvalidDocumentException ex) {
        log.warn("Invalid document error: {}", ex.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Document")
                .message(ex.getMessage())
                .path("/api/v1/customers")
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ValidationErrorResponseDTO>> handleValidationErrors(WebExchangeBindException ex) {
        log.warn("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponseDTO errorResponse = ValidationErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Request validation failed")
                .path("/api/v1/customers")
                .validationErrors(errors)
                .build();

        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleGenericError(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path("/api/v1/customers")
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}