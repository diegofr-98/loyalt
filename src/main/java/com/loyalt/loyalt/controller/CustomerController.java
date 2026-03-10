package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.customer.CreateCustomerRequest;
import com.loyalt.loyalt.dto.customer.CreateCustomerResponse;
import com.loyalt.loyalt.dto.customer.CustomerBusinessRequest;
import com.loyalt.loyalt.dto.customer.CustomerBusinessResponse;
import com.loyalt.loyalt.service.CustomerBusinessService;
import com.loyalt.loyalt.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerBusinessService customerBusinessService;
    public CustomerController(CustomerService customerService, CustomerBusinessService customerBusinessService){
        this.customerService = customerService;
        this.customerBusinessService = customerBusinessService;
    }

    @PostMapping()
    public ResponseEntity<CreateCustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request){
        CreateCustomerResponse customer = customerService.createCustomer(request);

        return ResponseEntity.ok(customer);

    }

    @PostMapping("/customer-business")
    public ResponseEntity<CustomerBusinessResponse> generateGoogleObject(@Valid @RequestBody CustomerBusinessRequest request) throws IOException {
        CustomerBusinessResponse response = customerBusinessService.joinBusiness(request.customerId(), request.businessId());
        return ResponseEntity.ok(response);
    }



}
