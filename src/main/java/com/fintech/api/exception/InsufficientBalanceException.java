// src/main/java/com/fintech/api/exception/InsufficientBalanceException.java
package com.fintech.api.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}