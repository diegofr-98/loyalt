package com.loyalt.loyalt.dto.customer;

import java.util.UUID;

public record CustomerBusinessRequest(UUID customerId, UUID businessId) {
}
