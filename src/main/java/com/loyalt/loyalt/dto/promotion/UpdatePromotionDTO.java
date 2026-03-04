package com.loyalt.loyalt.dto.promotion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;

public record UpdatePromotionDTO(

        @NotBlank
        String name,

        @NotNull
        OffsetDateTime startDate,

        @NotNull
        OffsetDateTime finishDate,

        @NotNull
        @Positive
        Integer points
) {}