// src/main/java/com/fintech/api/dto/CreateUserRequest.java
package com.fintech.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Хэрэглэгч үүсгэх хүсэлтийн бүтэц, validation дүрэмтэй
public record CreateUserRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid address")
        String email
) {}