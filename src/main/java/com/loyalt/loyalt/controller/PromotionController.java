package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.promotion.CreatePromotionDTO;
import com.loyalt.loyalt.dto.promotion.PromotionResponseDTO;
import com.loyalt.loyalt.dto.promotion.UpdatePromotionDTO;
import com.loyalt.loyalt.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionService service;

    public PromotionController(PromotionService service) {
        this.service = service;
    }

    @PostMapping
    public PromotionResponseDTO create(
            @RequestBody @Valid CreatePromotionDTO dto
    ) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public PromotionResponseDTO update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdatePromotionDTO dto
    ) {
        return service.update(id, dto);
    }

    @GetMapping("/{id}")
    public PromotionResponseDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/business/{businessId}")
    public List<PromotionResponseDTO> getByBusiness(
            @PathVariable UUID businessId
    ) {
        return service.getByBusiness(businessId);
    }
}