package com.loyalt.loyalt.dto.customer;

import java.util.UUID;

public record RedemptionResponse(UUID customerId, int pointsUsed) {
}
