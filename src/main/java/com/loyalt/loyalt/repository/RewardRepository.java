package com.loyalt.loyalt.repository;

import com.loyalt.loyalt.model.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RewardRepository extends JpaRepository<Reward, UUID> {

    List<Reward> findByBusinessId(UUID businessId);

    List<Reward> findByBusinessIdAndActiveTrue(UUID businessId);
}