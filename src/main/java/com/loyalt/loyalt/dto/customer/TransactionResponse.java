package com.loyalt.loyalt.dto.customer;

import java.util.UUID;

public record TransactionResponse(UUID customerId, int pointsAdded) {
}
