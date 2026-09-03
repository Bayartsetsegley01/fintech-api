// src/main/java/com/fintech/api/dto/CreateAccountRequest.java
package com.fintech.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(

        @NotNull(message = "userId is required")
        Long userId,

        @NotBlank(message = "accountNumber is required")
        String accountNumber,

        @NotNull(message = "initialBalance is required")
        @DecimalMin(value = "0.0", message = "initialBalance cannot be negative")
        BigDecimal initialBalance
) {}