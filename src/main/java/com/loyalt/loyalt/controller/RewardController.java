package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.reward.*;
import com.loyalt.loyalt.service.RewardService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardController {

    private final RewardService service;

    public RewardController(RewardService service) {
        this.service = service;
    }

    @PostMapping
    public RewardResponseDTO create(@RequestBody @Valid CreateRewardDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public RewardResponseDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/business/{businessId}")
    public List<RewardResponseDTO> getByBusiness(@PathVariable UUID businessId) {
        return service.getByBusiness(businessId);
    }

    @PutMapping("/{id}")
    public RewardResponseDTO update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateRewardDTO dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
