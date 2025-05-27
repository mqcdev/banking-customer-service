package com.nttdata.banking.customer.utils;

import com.nttdata.banking.customer.enums.DocumentType;
import com.nttdata.banking.customer.exception.InvalidDocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DocumentValidator {

    public void validateDocumentFormat(DocumentType documentType, String documentNumber) {
        log.debug("Validating document: type={}, number={}", documentType, documentNumber);

        if (documentType == null) {
            throw new InvalidDocumentException("Document type cannot be null");
        }

        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            throw new InvalidDocumentException("Document number cannot be empty");
        }

        String cleanDocumentNumber = documentNumber.trim();

        if (!documentType.isValidFormat(cleanDocumentNumber)) {
            throw new InvalidDocumentException(
                    String.format("Invalid format for document type %s. Expected pattern: %s",
                            documentType.getName(), documentType.getPattern())
            );
        }

        log.debug("Document validation successful for type={}, number={}", documentType, documentNumber);
    }

    public void validateRucFormat(String ruc) {
        if (ruc != null && !ruc.trim().isEmpty()) {
            log.debug("Validating RUC: {}", ruc);

            String cleanRuc = ruc.trim();
            if (!cleanRuc.matches("^\\d{11}$")) {
                throw new InvalidDocumentException("RUC must be exactly 11 digits");
            }

            log.debug("RUC validation successful: {}", ruc);
        }
    }
}

