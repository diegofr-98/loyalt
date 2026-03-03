package com.loyalt.loyalt.dto;

import java.util.UUID;

public class CreateBusinessResponse {
           /* Business business = new Business();
        business.setName(companyName);
        business.setBusinessTypeId(businessTypeId);
        business.setOwnerId(ownerId);
        business.setProgramName(companyName + "Rewards");
        business.setGoogleClassId(googleClassId);*/
    private UUID businessId;
    private String companyName;
    private UUID businessTypeId;
    private UUID ownerId;
    private String programName;
    private String googleClassId;

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public UUID getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(UUID businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getGoogleClassId() {
        return googleClassId;
    }

    public void setGoogleClassId(String googleClassId) {
        this.googleClassId = googleClassId;
    }
}
