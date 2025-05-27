package com.nttdata.banking.customer.dto.request;

import com.nttdata.banking.customer.enums.CustomerType;
import com.nttdata.banking.customer.enums.DocumentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomerRequestDTO {
    @NotEmpty(message = "Customer type cannot be empty")
    @Builder.Default
    private Set<CustomerType> customerType = new HashSet<>();

    // Personal customer fields
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @NotNull(message = "Document type is required")
    private DocumentType documentType;

    @NotBlank(message = "Document number is required")
    @Size(max = 12, message = "Document number cannot exceed 12 characters")
    private String documentNumber;

    // Business customer fields
    @Size(max = 200, message = "Company name cannot exceed 200 characters")
    private String companyName;

    @Pattern(regexp = "^\\d{11}$|^$", message = "RUC must be exactly 11 digits")
    private String ruc;

    // Common fields
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number format is invalid")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @NotBlank(message = "Address is required")
    @Size(max = 300, message = "Address cannot exceed 300 characters")
    private String address;
}
