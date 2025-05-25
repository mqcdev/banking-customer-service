package com.nttdata.banking.customer.models;

import com.nttdata.banking.customer.enums.CustomerType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;
    private List<CustomerType> customerType;
    private String firstName;
    private String lastName;
    private String documentType;
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