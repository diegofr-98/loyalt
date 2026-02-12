package com.loyalt.loyalt.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name= "user_business")
public class UserBusiness {
    protected  UserBusiness(){}
    @Id
    private UUID uuid;
    public UUID userId;
    public UUID businessId;
    public Date joinedAt;
    public String status;







}
