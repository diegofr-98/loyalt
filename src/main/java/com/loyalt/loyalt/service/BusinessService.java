package com.loyalt.loyalt.service;

import com.loyalt.loyalt.auth.SecurityUtils;
import com.loyalt.loyalt.dto.business.CreateBusinessRequest;
import com.loyalt.loyalt.dto.business.CreateBusinessResponse;
import com.loyalt.loyalt.exception.BadRequestException;
import com.loyalt.loyalt.exception.ConflictException;
import com.loyalt.loyalt.integration.googlewallet.GoogleWalletClassService;
import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.repository.BusinessRepository;
import com.loyalt.loyalt.repository.BusinessTypeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final GoogleWalletClassService googleWalletService;
    private final BusinessTypeRepository businessTypeRepository;
    private String issuerId;

    final String DEFAULT_LOGO = "https://rifsoxpgwhcnyklspgsc.supabase.co/storage/v1/object/public/business-logos/generic-logo.png";
    public BusinessService(BusinessRepository businessRepository,
                           @Value("${google.wallet.issuer-id}") String issuerId,
                           GoogleWalletClassService googleWalletService,
                           BusinessTypeRepository businessTypeRepository
    ){
        this.businessRepository = businessRepository;
        this.googleWalletService = googleWalletService;
        this.issuerId = issuerId;
        this.businessTypeRepository = businessTypeRepository;
    }


    public List<Business> getAllBusinesses(){
        Map<String, String> business = new HashMap<>();
        return this.businessRepository.findAll();

    }

    public Business getByOwnerId(UUID ownerId) {
        return this.businessRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public CreateBusinessResponse createBusiness(CreateBusinessRequest request) throws IOException {

        if (request.businessName() == null || request.businessName().isBlank()) {
            throw new BadRequestException("Business name is required");
        }

        if (request.businessName().length() > 100) {
            throw new BadRequestException("Business name is too long");
        }

        if(!businessTypeRepository.existsById(UUID.fromString(request.businessTypeId()))){
            throw new BadRequestException("BusinessTypeId is invalid or does not exists");

        }

        if (request.logoUrl() == null || request.logoUrl().isBlank()) {
            throw new BadRequestException("Logo URL is required");
        }

        if (!request.logoUrl().startsWith("http")) {
            throw new BadRequestException("Logo URL must be a valid URL");
        }


        UUID businessTypeId = UUID.fromString(request.businessTypeId());
        UUID businessId = UUID.randomUUID();
        String businessName = request.businessName();
        UUID ownerId = SecurityUtils.getCurrentUserId();

        boolean exists = businessRepository.existsByNameAndOwnerId(businessName, ownerId);

        if (exists) {
            throw new ConflictException("Business already exists");
        }

        String googleClassId = issuerId + "." + businessId;
        String programName = businessName + " Rewards";
        String businessLogo = request.logoUrl();



        String googleClassIdFromGoogle = googleWalletService.createLoyaltyClass(googleClassId,businessName,
                programName, businessLogo);


        Business business = new Business();
        business.setUuid(businessId);
        business.setName(businessName);
        business.setBusinessTypeId(businessTypeId);
        business.setOwnerId(ownerId);
        business.setProgramName(programName);
        business.setGoogleClassId(googleClassIdFromGoogle);
        business.setLogoUrl(businessLogo);
        business.setActive(true);


        Business save = businessRepository.save(business);

        return new CreateBusinessResponse(save.getUuid(),save.getName(),save.getProgramName(), save.getLogoURL() );

    }
}
