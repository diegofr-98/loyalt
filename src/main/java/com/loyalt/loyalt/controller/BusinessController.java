package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
