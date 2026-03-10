package com.loyalt.loyalt.service;

import com.loyalt.loyalt.dto.customer.CustomerBusinessResponse;
import com.loyalt.loyalt.integration.googlewallet.GoogleWalletJwtService;
import com.loyalt.loyalt.integration.googlewallet.GoogleWalletObjectService;
import com.loyalt.loyalt.model.entity.Business;
import com.loyalt.loyalt.model.entity.Customer;
import com.loyalt.loyalt.model.entity.CustomerBusiness;
import com.loyalt.loyalt.model.enums.MembershipStatus;
import com.loyalt.loyalt.repository.BusinessRepository;
import com.loyalt.loyalt.repository.CustomerBusinessRepository;
import com.loyalt.loyalt.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class CustomerBusinessService {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final CustomerBusinessRepository customerBusinessRepository;

    private final GoogleWalletObjectService googleWalletObjectService;
    private final GoogleWalletJwtService googleWalletJwtService;

    public CustomerBusinessService(CustomerRepository customerRepository, BusinessRepository businessRepository,
                                   CustomerBusinessRepository customerBusinessRepository, GoogleWalletObjectService googleWalletObjectService,
                                   GoogleWalletJwtService googleWalletJwtService){
        this.customerRepository = customerRepository;
        this.businessRepository = businessRepository;
        this.customerBusinessRepository = customerBusinessRepository;
        this.googleWalletObjectService = googleWalletObjectService;
        this.googleWalletJwtService = googleWalletJwtService;
    }

    @Transactional
    public CustomerBusinessResponse joinBusiness(UUID customerId, UUID businessId)
            throws IOException {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        customerBusinessRepository
                .findByCustomerUuidAndBusinessUuid(customerId, businessId)
                .ifPresent(m -> {
                    throw new RuntimeException("Customer already joined this business");
                });

        CustomerBusiness membership = new CustomerBusiness();

        membership.setCustomer(customer);
        membership.setBusiness(business);
        membership.setStatus(String.valueOf(MembershipStatus.ACTIVE)
                );
        membership.setPointsBalance(0);

        customerBusinessRepository.save(membership);

        String loyaltyObjectId =
                googleWalletObjectService.createGoogleWalletObject(
                        membership.getUuid(),
                        business.getGoogleClassId()
                );

        membership.setGoogleObjectId(loyaltyObjectId);

        customerBusinessRepository.save(membership);

        String addToWalletUrl =
                googleWalletJwtService.createSaveLink(loyaltyObjectId);

        return new CustomerBusinessResponse(
                membership.getUuid(),
                addToWalletUrl
        );
    }


}
