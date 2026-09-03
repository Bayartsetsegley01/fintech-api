// src/main/java/com/fintech/api/exception/AccountNotFoundException.java
package com.fintech.api.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}