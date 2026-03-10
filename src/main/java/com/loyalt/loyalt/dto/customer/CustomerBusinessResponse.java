package com.loyalt.loyalt.dto.customer;

import java.util.UUID;

public record CustomerBusinessResponse(UUID membershipId, String addToWalletUrl) {
}
