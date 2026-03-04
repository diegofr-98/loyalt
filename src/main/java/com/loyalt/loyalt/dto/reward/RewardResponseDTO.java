package com.loyalt.loyalt.dto.reward;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RewardResponseDTO(
        UUID uuid,
        UUID businessId,
        String name,
        Integer costPoints,
        String imgUrl,
        Boolean active,
        OffsetDateTime createdAt
) {}