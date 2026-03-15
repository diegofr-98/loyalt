package com.loyalt.loyalt.dto.customer;

import java.util.UUID;

public record CreateCustomerResponse (UUID customerId, String email, String phoneNumber){
}
