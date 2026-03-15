package com.loyalt.loyalt.service;

import com.loyalt.loyalt.dto.promotion.CreatePromotionDTO;
import com.loyalt.loyalt.dto.promotion.PromotionResponseDTO;
import com.loyalt.loyalt.dto.promotion.UpdatePromotionDTO;
import com.loyalt.loyalt.model.entity.Promotion;
import com.loyalt.loyalt.repository.PromotionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PromotionService {

    private final PromotionRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(PromotionService.class);

    public PromotionService(PromotionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PromotionResponseDTO create(CreatePromotionDTO dto) {
        logger.info("Creating promotion with data: {}", dto);
        if (dto.finishDate().isBefore(dto.startDate())) {
            throw new IllegalArgumentException("finishDate cannot be before startDate");
        }

        Promotion promotion = new Promotion();
        promotion.setBusinessId(dto.businessId());
        promotion.setName(dto.name());
        promotion.setStartDate(dto.startDate());
        promotion.setFinishDate(dto.finishDate());
        promotion.setPoints(dto.points());

        Promotion saved = repository.save(promotion);

        return mapToResponse(saved);
    }

    @Transactional
    public PromotionResponseDTO update(UUID id, UpdatePromotionDTO dto) {

        Promotion promotion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        if (dto.finishDate().isBefore(dto.startDate())) {
            throw new IllegalArgumentException("finishDate cannot be before startDate");
        }

        promotion.setName(dto.name());
        promotion.setStartDate(dto.startDate());
        promotion.setFinishDate(dto.finishDate());
        promotion.setPoints(dto.points());

        Promotion updated = repository.save(promotion);

        return mapToResponse(updated);
    }

    public List<PromotionResponseDTO> getByBusiness(UUID businessId) {
        return repository.findByBusinessId(businessId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PromotionResponseDTO getById(UUID id) {
        Promotion promotion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        return mapToResponse(promotion);
    }

    private PromotionResponseDTO mapToResponse(Promotion promotion) {
        return new PromotionResponseDTO(
                promotion.getUuid(),
                promotion.getBusinessId(),
                promotion.getName(),
                promotion.getStartDate(),
                promotion.getFinishDate(),
                promotion.getPoints(),
                promotion.getCreatedAt()
        );
    }
}