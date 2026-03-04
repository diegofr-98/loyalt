package com.loyalt.loyalt.model.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "promotion", schema = "public")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "business_id", nullable = false)
    private UUID businessId;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "finish_date", nullable = false)
    private OffsetDateTime finishDate;

    @Column(nullable = false)
    private Integer points;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public UUID getBusinessId() { return businessId; }
    public void setBusinessId(UUID businessId) { this.businessId = businessId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public OffsetDateTime getStartDate() { return startDate; }
    public void setStartDate(OffsetDateTime startDate) { this.startDate = startDate; }

    public OffsetDateTime getFinishDate() { return finishDate; }
    public void setFinishDate(OffsetDateTime finishDate) { this.finishDate = finishDate; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
}