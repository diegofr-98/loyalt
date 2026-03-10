package com.loyalt.loyalt.repository;

import com.loyalt.loyalt.model.entity.CustomerBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerBusinessRepository extends JpaRepository<CustomerBusiness, UUID> {
    Optional<CustomerBusiness> findByCustomerUuidAndBusinessUuid(UUID customerId, UUID businessId);
}
