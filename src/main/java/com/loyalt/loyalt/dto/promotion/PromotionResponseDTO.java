package com.loyalt.loyalt.dto.promotion;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PromotionResponseDTO(
        UUID uuid,
        UUID businessId,
        String name,
        OffsetDateTime startDate,
        OffsetDateTime finishDate,
        Integer points,
        OffsetDateTime createdAt
) {}