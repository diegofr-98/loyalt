package com.loyalt.loyalt.service;


import com.loyalt.loyalt.dto.business.BusinessCustomerDTO;
import com.loyalt.loyalt.dto.customer.*;
import com.loyalt.loyalt.exception.ConflictException;
import com.loyalt.loyalt.integration.googlewallet.WalletJwtService;
import com.loyalt.loyalt.integration.googlewallet.WalletObjectService;
import com.loyalt.loyalt.model.entity.*;
import com.loyalt.loyalt.model.enums.MembershipStatus;
import com.loyalt.loyalt.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final CustomerBusinessRepository customerBusinessRepository;
    private final WalletObjectService walletObjectService;
    private final WalletJwtService walletJwtService;
    private final CustomerTransactionRepository customerTransactionRepository;
    private final RedemptionRepository redemptionRepository;
    public CustomerService(CustomerRepository customerRepository,
                           BusinessRepository businessRepository,
                           CustomerBusinessRepository customerBusinessRepository,
                           WalletObjectService walletObjectService,
                           WalletJwtService walletJwtService,
                           CustomerTransactionRepository customerTransactionRepository,
                           RedemptionRepository redemptionRepository){
        this.customerRepository = customerRepository;
        this.businessRepository = businessRepository;
        this.customerBusinessRepository = customerBusinessRepository;
        this.walletObjectService = walletObjectService;
        this.walletJwtService = walletJwtService;
        this.customerTransactionRepository = customerTransactionRepository;
        this.redemptionRepository = redemptionRepository;
    }

    @Transactional
    public CreateCustomerResponse createCustomer (CreateCustomerRequest request){

        if(customerRepository.existsByEmail(request.email())){
            throw new ConflictException("This email is already registered");
        }

        Customer customer = new Customer();
        customer.setEmail(request.email().toLowerCase().trim());
        customer.setPhoneNumber(request.phoneNumber());

        Customer saved = customerRepository.save(customer);
        return new CreateCustomerResponse(saved.getUuid(), saved.getEmail(), saved.getPhoneNumber());

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
                walletObjectService.createGoogleWalletObject(
                        membership.getUuid(),
                        business.getGoogleClassId()
                );

        membership.setGoogleObjectId(loyaltyObjectId);

        customerBusinessRepository.save(membership);

        String addToWalletUrl =
                walletJwtService.createSaveLink(loyaltyObjectId);

        return new CustomerBusinessResponse(
                membership.getUuid(),
                addToWalletUrl
        );
    }

    public Page<BusinessCustomerDTO> getBusinessCustomers(UUID businessId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return customerBusinessRepository.findCustomersByBusinessId(businessId, pageable);
    }

    public TransactionResponse createTransaction(TransactionRequest request){

        CustomerTransaction transaction = new CustomerTransaction();
        transaction.setBusinessId(UUID.fromString(request.businessId()));
        transaction.setCustomerId(UUID.fromString(request.customerId()));
        transaction.setPoints(request.points());
        CustomerTransaction saved = customerTransactionRepository.save(transaction);

        return new TransactionResponse(saved.getCustomerId(), saved.getPoints());
    }

    public RedemptionResponse createRedemption(RedemptionRequest request){

        Redemption redemption = new Redemption();
        redemption.setBusinessId(UUID.fromString(request.businessId()));
        redemption.setCustomerId(UUID.fromString(request.customerId()));
        redemption.setRewardId(UUID.fromString(request.rewardId()));
        redemption.setPointsUsed(request.pointsUsed());

        Redemption saved = redemptionRepository.save(redemption);

        return new RedemptionResponse(saved.getCustomerId(), saved.getPointsUsed());

    }
}
