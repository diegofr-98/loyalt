package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.business.CreateBusinessRequest;
import com.loyalt.loyalt.dto.business.CreateBusinessResponse;
import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.service.BusinessService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {
    private final BusinessService businessService;

    public BusinessController(BusinessService businessService){

        this.businessService = businessService;
    }

    @GetMapping()
    public ResponseEntity<List<Business>> getAllBusiness(){
        List<Business> businesses = this.businessService.getAllBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @PostMapping()
    public ResponseEntity<CreateBusinessResponse> create(@Valid @RequestBody CreateBusinessRequest request) throws IOException {
        CreateBusinessResponse createBusinessResponse = businessService.createBusiness(request);
        return ResponseEntity.ok(createBusinessResponse);

    }
}
