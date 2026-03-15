package com.loyalt.loyalt.dto.reward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateRewardDTO(

        @NotNull
        UUID businessId,

        @NotBlank
        String name,

        @NotNull
        @Positive
        Integer costPoints,

        String imgUrl
) {}