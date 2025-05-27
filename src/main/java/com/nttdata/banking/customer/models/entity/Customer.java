package com.nttdata.banking.customer.models.entity;

import com.nttdata.banking.customer.enums.CustomerType;
import com.nttdata.banking.customer.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;

    @Builder.Default
    private Set<CustomerType> customerType = new HashSet<>();

    private String firstName;

    private String lastName;

    private DocumentType documentType;

    @Indexed(unique = true)
    private String documentNumber;

    private String companyName;

    @Indexed(unique = true, sparse = true)
    private String ruc;

    private String phoneNumber;

    @Indexed(unique = true)
    private String email;

    private String address;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @Builder.Default
    private Boolean active = true;

    public boolean isPersonalCustomer() {
        return customerType.contains(CustomerType.PERSONAL);
    }

    public boolean isBusinessCustomer() {
        return customerType.contains(CustomerType.BUSINESS);
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        updateTimestamp();
    }

    public void deactivate() {
        this.active = false;
        updateTimestamp();
    }
}

