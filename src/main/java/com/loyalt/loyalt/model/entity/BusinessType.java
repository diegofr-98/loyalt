package com.loyalt.loyalt.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "business_type")
public class BusinessType {

    @Id
    private UUID uuid;
    private String Retail;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRetail() {
        return Retail;
    }

    public void setRetail(String retail) {
        Retail = retail;
    }
}
