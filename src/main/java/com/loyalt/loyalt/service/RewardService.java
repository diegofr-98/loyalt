package com.loyalt.loyalt.service;

import com.loyalt.loyalt.dto.reward.*;
import com.loyalt.loyalt.model.entity.Reward;
import com.loyalt.loyalt.repository.RewardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RewardService {

    private final RewardRepository repository;

    public RewardService(RewardRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RewardResponseDTO create(CreateRewardDTO dto) {

        Reward reward = new Reward();
        reward.setBusinessId(dto.businessId());
        reward.setName(dto.name());
        reward.setCostPoints(dto.costPoints());
        reward.setImgUrl(dto.imgUrl());
        reward.setActive(true);

        return mapToResponse(repository.save(reward));
    }

    public RewardResponseDTO getById(UUID id) {
        Reward reward = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));

        return mapToResponse(reward);
    }

    public List<RewardResponseDTO> getByBusiness(UUID businessId) {
        return repository.findByBusinessId(businessId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public RewardResponseDTO update(UUID id, UpdateRewardDTO dto) {

        Reward reward = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));

        reward.setName(dto.name());
        reward.setCostPoints(dto.costPoints());
        reward.setImgUrl(dto.imgUrl());
        reward.setActive(dto.active());

        return mapToResponse(repository.save(reward));
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private RewardResponseDTO mapToResponse(Reward reward) {
        return new RewardResponseDTO(
                reward.getUuid(),
                reward.getBusinessId(),
                reward.getName(),
                reward.getCostPoints(),
                reward.getImgUrl(),
                reward.getActive(),
                reward.getCreatedAt()
        );
    }
}
