package com.nttdata.banking.customer.utils;

import com.nttdata.banking.customer.dto.request.CustomerRequestDTO;
import com.nttdata.banking.customer.enums.CustomerType;
import com.nttdata.banking.customer.exception.InvalidDocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerValidator {

    private final DocumentValidator documentValidator;

    /**
     * Validates customer request data according to business rules
     *
     * @param request the customer request to validate
     * @throws InvalidDocumentException if validation fails
     */
    public void validateCustomerRequest(CustomerRequestDTO request) {
        log.debug("Validating customer request: {}", request);

        validateCustomerTypes(request);
        validatePersonalCustomerFields(request);
        validateBusinessCustomerFields(request);
        validateDocuments(request);

        log.debug("Customer request validation successful");
    }

    /**
     * Validates customer types are properly set
     */
    private void validateCustomerTypes(CustomerRequestDTO request) {
        if (request.getCustomerType() == null || request.getCustomerType().isEmpty()) {
            throw new InvalidDocumentException("At least one customer type must be specified");
        }
    }

    /**
     * Validates personal customer specific fields
     */
    private void validatePersonalCustomerFields(CustomerRequestDTO request) {
        if (request.getCustomerType().contains(CustomerType.PERSONAL)) {
            if (isBlank(request.getFirstName())) {
                throw new InvalidDocumentException("First name is required for personal customers");
            }
            if (isBlank(request.getLastName())) {
                throw new InvalidDocumentException("Last name is required for personal customers");
            }
        }
    }

    /**
     * Validates business customer specific fields
     */
    private void validateBusinessCustomerFields(CustomerRequestDTO request) {
        if (request.getCustomerType().contains(CustomerType.BUSINESS)) {
            if (isBlank(request.getCompanyName())) {
                throw new InvalidDocumentException("Company name is required for business customers");
            }
            if (isBlank(request.getRuc())) {
                throw new InvalidDocumentException("RUC is required for business customers");
            }
            documentValidator.validateRucFormat(request.getRuc());
        }
    }

    /**
     * Validates document format
     */
    private void validateDocuments(CustomerRequestDTO request) {
        documentValidator.validateDocumentFormat(request.getDocumentType(), request.getDocumentNumber());
    }

    /**
     * Utility method to check if string is blank
     */
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
