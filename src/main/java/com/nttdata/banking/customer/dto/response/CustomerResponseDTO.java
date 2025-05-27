package com.nttdata.banking.customer.dto.response;

import com.nttdata.banking.customer.enums.CustomerType;
import com.nttdata.banking.customer.enums.DocumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CustomerResponseDTO {
    private String id;
    private Set<CustomerType> customerType;
    private String firstName;
    private String lastName;
    private DocumentType documentType;
    private String documentNumber;
    private String companyName;
    private String ruc;
    private String phoneNumber;
    private String email;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;
}
