package com.loyalt.loyalt.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_business",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "business_id"})
    })
public class CustomerBusiness {

    @Id
    @GeneratedValue
    @Column(name="UUID")
    private UUID uuid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "uuid"
    )
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "business_id",
            referencedColumnName = "uuid"
    )
    private Business business;
    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private OffsetDateTime joinedAt;
    @Column(name = "points_balance")
    private Integer pointsBalance;
    @Column(name = "status")
    private String status;
    @Column(name = "google_object_id")
    private String googleObjectId;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public OffsetDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(OffsetDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Integer getPointsBalance() {
        return pointsBalance;
    }

    public void setPointsBalance(Integer pointsBalance) {
        this.pointsBalance = pointsBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoogleObjectId() {
        return googleObjectId;
    }

    public void setGoogleObjectId(String googleObjectId) {
        this.googleObjectId = googleObjectId;
    }
}
