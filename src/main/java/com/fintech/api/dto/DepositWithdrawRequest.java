// src/main/java/com/fintech/api/dto/DepositWithdrawRequest.java
package com.fintech.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositWithdrawRequest(

        @NotNull(message = "accountId is required")
        Long accountId,

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be greater than zero")
        BigDecimal amount,

        String description
) {}