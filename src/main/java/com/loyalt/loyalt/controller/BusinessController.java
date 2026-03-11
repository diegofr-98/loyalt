package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.business.BusinessCustomerDTO;
import com.loyalt.loyalt.dto.business.CreateBusinessRequest;
import com.loyalt.loyalt.dto.business.CreateBusinessResponse;
import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.service.BusinessService;
import com.loyalt.loyalt.service.CustomerBusinessService;
import com.loyalt.loyalt.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {
    private final BusinessService businessService;
    private final CustomerBusinessService customerBusinessService;

    public BusinessController(BusinessService businessService, CustomerBusinessService customerBusinessService){
        this.customerBusinessService = customerBusinessService;
        this.businessService = businessService;
    }

    @GetMapping()
    public ResponseEntity<List<Business>> getAllBusiness(){
        List<Business> businesses = this.businessService.getAllBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @GetMapping("/{ownerId}")
    public Business getByOwnerId(@Valid @PathVariable UUID ownerId) {
        return businessService.getByOwnerId(ownerId);
    }

    @GetMapping("/{businessId}/customers")
    public Page<BusinessCustomerDTO> getBusinessCustomers(
            @PathVariable UUID businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return customerBusinessService.getBusinessCustomers(businessId, page, size);
    }

    @PostMapping()
    public ResponseEntity<CreateBusinessResponse> create(@Valid @RequestBody CreateBusinessRequest request) throws IOException {
        CreateBusinessResponse createBusinessResponse = businessService.createBusiness(request);
        return ResponseEntity.ok(createBusinessResponse);

    }
}
