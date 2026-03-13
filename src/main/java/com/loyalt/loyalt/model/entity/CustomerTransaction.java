package com.loyalt.loyalt.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_transaction")
public class CustomerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private UUID businessId;
    private UUID customerId;
    private UUID promotionId;
    private int points;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }
    public UUID getCustomerId() {
        return customerId;
    }
    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
