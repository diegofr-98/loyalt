package com.loyalt.loyalt.model.entity;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String name;
    private UUID businessTypeId;
    private Date createdAt;
    private UUID ownerId;


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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }



}
