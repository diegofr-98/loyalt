package com.loyalt.loyalt.dto.customer;

public record RedemptionRequest(String businessId, String rewardId, String customerId, int pointsUsed) {
}
