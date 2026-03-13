package com.loyalt.loyalt.repository;

import com.loyalt.loyalt.model.entity.Redemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RedemptionRepository extends JpaRepository<Redemption, UUID> {
}
