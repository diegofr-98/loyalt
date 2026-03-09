package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.customer.CreateCustomerRequest;
import com.loyalt.loyalt.dto.customer.CreateCustomerResponse;
import com.loyalt.loyalt.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping()
    public ResponseEntity<CreateCustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request){
        CreateCustomerResponse customer = customerService.createCustomer(request);

        return ResponseEntity.ok(customer);


    }


}
