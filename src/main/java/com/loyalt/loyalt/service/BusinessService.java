package com.loyalt.loyalt.service;

import com.loyalt.loyalt.auth.SecurityUtils;
import com.loyalt.loyalt.dto.CreateBusinessRequest;
import com.loyalt.loyalt.dto.CreateBusinessResponse;
import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private String issuerId;

    public BusinessService(BusinessRepository businessRepository, @Value("${google.wallet.issuer-id}") String issuerId
    ){
        this.businessRepository = businessRepository;
        this.issuerId = issuerId;
    }


    public List<Business> getAllBusinesses(){
        Map<String, String> business = new HashMap<>();
        return this.businessRepository.findAll();

    }

    public CreateBusinessResponse createBusiness(CreateBusinessRequest request){
        //del request necesito: name, business_type_id
        //this.businessRepository.save();

        //Parsing businessTypeId from String to UUID
        UUID businessId = UUID.randomUUID();
        String companyName = request.getCompanyName();
        UUID businessTypeId = UUID.fromString(request.getBusinessTypeId());
        UUID ownerId = SecurityUtils.getCurrentUserId();
        String googleClassId = issuerId + "." + businessId;



        /*Business business = new Business();
        business.setUuid(businessId);
        business.setName(companyName);
        business.setBusinessTypeId(businessTypeId);
        business.setOwnerId(ownerId);
        business.setProgramName(companyName + "Rewards");
        business.setGoogleClassId(googleClassId);*/
        CreateBusinessResponse response = new CreateBusinessResponse();
        response.setBusinessId(businessId);
        response.setCompanyName(companyName);
        response.setBusinessTypeId(businessTypeId);
        response.setOwnerId(ownerId);
        response.setProgramName(companyName + " Rewards");
        response.setGoogleClassId(googleClassId);




        return response;
    }
}
