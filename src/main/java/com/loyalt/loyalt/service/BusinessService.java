package com.loyalt.loyalt.service;

import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.repository.BusinessRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository){
        this.businessRepository = businessRepository;
    }


    public List<Business> getAllBusinesses(){
        Map<String, String> business = new HashMap<>();
        return this.businessRepository.findAll();

    }
}
