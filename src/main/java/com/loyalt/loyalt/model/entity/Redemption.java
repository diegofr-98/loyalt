package com.loyalt.loyalt.model.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="redemption")
public class Redemption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private UUID businessId;
    private UUID rewardId;
    private UUID customerId;
    private int pointsUsed;

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

    public UUID getRewardId() {
        return rewardId;
    }

    public void setRewardId(UUID rewardId) {
        this.rewardId = rewardId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(int pointsUsed) {
        this.pointsUsed = pointsUsed;
    }
}
