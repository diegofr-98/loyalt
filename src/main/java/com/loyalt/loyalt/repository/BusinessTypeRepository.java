package com.loyalt.loyalt.repository;

import com.loyalt.loyalt.model.entity.BusinessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, UUID> {
    boolean existsById(UUID uuid);
}
