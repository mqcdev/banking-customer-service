package com.nttdata.banking.customer.mapper;

import com.nttdata.banking.customer.dto.request.CustomerRequestDTO;
import com.nttdata.banking.customer.dto.response.CustomerResponseDTO;
import com.nttdata.banking.customer.models.entity.Customer;
import org.mapstruct.*;
import java.time.LocalDateTime;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {LocalDateTime.class}
)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    Customer toEntity(CustomerRequestDTO requestDto);

    CustomerResponseDTO toResponseDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDto(CustomerRequestDTO requestDto, @MappingTarget Customer existingCustomer);
}
