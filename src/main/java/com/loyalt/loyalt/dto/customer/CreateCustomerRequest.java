package com.loyalt.loyalt.dto.customer;

import jakarta.validation.constraints.*;

public record CreateCustomerRequest (
        @NotEmpty(message = "Email is required")
        @Email(message = "Invalid email format") String email,
        @NotEmpty(message = "Phone number is required")
        String phoneNumber){}
