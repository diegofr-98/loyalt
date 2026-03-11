package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.analytics.CustomerBusinessAnalyticsDTO;
import com.loyalt.loyalt.service.AnalyticsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/customer-business/{businessId}")
    public CustomerBusinessAnalyticsDTO getCustomerBusinessAnalytics(@Valid @PathVariable UUID businessId) {
        return service.getCustomerBusinessAnalytics(businessId);
    }
}