package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.customer.*;
import com.loyalt.loyalt.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping("/customer-business")
    public ResponseEntity<CustomerBusinessResponse> generateGoogleObject(@Valid @RequestBody CustomerBusinessRequest request) throws IOException {
        CustomerBusinessResponse response = customerService.joinBusiness(request.customerId(), request.businessId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request){
        TransactionResponse response = customerService.createTransaction(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/redemption")
    public ResponseEntity<RedemptionResponse> createRedemption(@Valid @RequestBody RedemptionRequest request){
        RedemptionResponse response = customerService.createRedemption(request);
        return ResponseEntity.ok(response);

    }



}
