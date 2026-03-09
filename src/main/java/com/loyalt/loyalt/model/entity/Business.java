package com.loyalt.loyalt.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

import java.util.UUID;

@Entity
@Table(name = "business")
public class Business {

    @Id
    private UUID uuid;
    private String name;
    private UUID businessTypeId;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    private UUID ownerId;
    private String googleClassId;
    private String programName;
    private String logoUrl;
    private Boolean isActive;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(UUID businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getGoogleClassId() {
        return googleClassId;
    }

    public void setGoogleClassId(String googleClassId) {
        this.googleClassId = googleClassId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getLogoURL() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
