package com.loyalt.loyalt.service;

import com.loyalt.loyalt.auth.SecurityUtils;
import com.loyalt.loyalt.dto.business.CreateBusinessRequest;
import com.loyalt.loyalt.dto.business.CreateBusinessResponse;
import com.loyalt.loyalt.exception.BadRequestException;
import com.loyalt.loyalt.exception.ConflictException;
import com.loyalt.loyalt.integration.googlewallet.WalletClassService;
import com.loyalt.loyalt.config.GoogleWalletProperties;
import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.repository.BusinessRepository;
import com.loyalt.loyalt.repository.BusinessTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final WalletClassService walletClassService;
    private final BusinessTypeRepository businessTypeRepository;
    private final GoogleWalletProperties googleWalletProperties;

    public BusinessService(BusinessRepository businessRepository,
                           WalletClassService walletClassService,
                           BusinessTypeRepository businessTypeRepository,
                           GoogleWalletProperties googleWalletProperties
    ){
        this.businessRepository = businessRepository;
        this.walletClassService = walletClassService;
        this.businessTypeRepository = businessTypeRepository;
        this.googleWalletProperties = googleWalletProperties;
    }


    public List<Business> getAllBusinesses(){
        return this.businessRepository.findAll();

    }

    public Business getByOwnerId(UUID ownerId) {
        return this.businessRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public CreateBusinessResponse createBusiness(CreateBusinessRequest request){

        validateRequest(request);

        UUID businessTypeId = UUID.fromString(request.businessTypeId());
        UUID businessId = UUID.randomUUID();
        String businessName = request.businessName();
        UUID ownerId = SecurityUtils.getCurrentUserId();

        if (businessRepository.existsByNameAndOwnerId(businessName, ownerId)) {
            throw new ConflictException("Business already exists");
        }

        String googleClassId = googleWalletProperties.getIssuerId() + "." + businessId;
        String programName = businessName + " Rewards";
        String businessLogo = request.logoUrl();

        String createdClassId = walletClassService.createLoyaltyClass(googleClassId,businessName,
                programName, businessLogo);


        Business business = new Business();
        business.setUuid(businessId);
        business.setName(businessName);
        business.setBusinessTypeId(businessTypeId);
        business.setOwnerId(ownerId);
        business.setProgramName(programName);
        business.setGoogleClassId(createdClassId);
        business.setLogoUrl(businessLogo);
        business.setActive(true);


        Business saved = businessRepository.save(business);

        return new CreateBusinessResponse(saved.getUuid(),
                saved.getName(),
                saved.getProgramName(),
                saved.getLogoURL() );

    }

    private void validateRequest(CreateBusinessRequest request) {

        if (request.businessName() == null || request.businessName().isBlank()) {
            throw new BadRequestException("Business name is required");
        }

        if (request.businessName().length() > 100) {
            throw new BadRequestException("Business name is too long");
        }

        if (request.businessTypeId() == null) {
            throw new BadRequestException("BusinessTypeId is required");
        }

        UUID businessTypeId;

        try {
            businessTypeId = UUID.fromString(request.businessTypeId());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("BusinessTypeId is invalid");
        }

        if (!businessTypeRepository.existsById(businessTypeId)) {
            throw new BadRequestException("BusinessTypeId does not exist");
        }

        if (request.logoUrl() == null || request.logoUrl().isBlank()) {
            throw new BadRequestException("Logo URL is required");
        }

        if (!request.logoUrl().startsWith("http")) {
            throw new BadRequestException("Logo URL must be a valid URL");
        }
    }
}
