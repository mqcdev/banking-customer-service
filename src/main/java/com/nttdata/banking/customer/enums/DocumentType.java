package com.nttdata.banking.customer.enums;

import lombok.Getter;

@Getter
public enum DocumentType {
    DNI("DNI", 8, "^\\d{8}$"),
    PASSPORT("PASSPORT", 12, "^[A-Z0-9]{6,12}$"),
    RUC("RUC", 11, "^\\d{11}$"),
    FOREIGNER_CARD("FOREIGNER_CARD", 12, "^[A-Z0-9]{6,12}$");

    private final String name;
    private final int maxLength;
    private final String pattern;

    DocumentType(String name, int maxLength, String pattern) {
        this.name = name;
        this.maxLength = maxLength;
        this.pattern = pattern;
    }

    public boolean isValidFormat(String documentNumber) {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            return false;
        }
        return documentNumber.matches(this.pattern);
    }
}
