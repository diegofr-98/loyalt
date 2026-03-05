package com.loyalt.loyalt.service;

import com.loyalt.loyalt.auth.SecurityUtils;
import com.loyalt.loyalt.dto.CreateBusinessRequest;
import com.loyalt.loyalt.dto.CreateBusinessResponse;
import com.loyalt.loyalt.integration.GoogleWalletService;
import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final GoogleWalletService googleWalletService;
    private String issuerId;

    public BusinessService(BusinessRepository businessRepository, @Value("${google.wallet.issuer-id}") String issuerId, GoogleWalletService googleWalletService
    ){
        this.businessRepository = businessRepository;
        this.googleWalletService = googleWalletService;
        this.issuerId = issuerId;
    }


    public List<Business> getAllBusinesses(){
        Map<String, String> business = new HashMap<>();
        return this.businessRepository.findAll();

    }

    @Transactional
    public CreateBusinessResponse createBusiness(CreateBusinessRequest request) throws IOException {
        //del request necesito: name, business_type_id
        //this.businessRepository.save();

        //Parsing businessTypeId from String to UUID
        UUID businessId = UUID.randomUUID();
        String businessName = request.getBusinessName();
        UUID businessTypeId = UUID.fromString(request.getBusinessTypeId());
        UUID ownerId = SecurityUtils.getCurrentUserId();
        String googleClassId = issuerId + "." + businessId;
        String programName = businessName + " Rewards";

        String googleClassIdFromGoogle = googleWalletService.createLoyaltyClass(googleClassId,businessName, programName);


        Business business = new Business();
        business.setUuid(businessId);
        business.setName(businessName);
        business.setBusinessTypeId(businessTypeId);
        business.setOwnerId(ownerId);
        business.setProgramName(businessName + "Rewards");
        business.setGoogleClassId(googleClassIdFromGoogle);


        Business save = businessRepository.save(business);

        CreateBusinessResponse response = new CreateBusinessResponse();

        response.setCompanyName(save.getName());
        response.setBusinessId(save.getUuid());

        return response;
    }
}
