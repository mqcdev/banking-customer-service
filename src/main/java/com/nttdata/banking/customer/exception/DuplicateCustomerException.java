package com.nttdata.banking.customer.exception;


public class DuplicateCustomerException extends RuntimeException {

    public DuplicateCustomerException(String message) {
        super(message);
    }

    public DuplicateCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
