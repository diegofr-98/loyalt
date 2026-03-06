package com.loyalt.loyalt.dto;

import java.util.UUID;

public class CreateBusinessRequest {
    String businessName;
    String businessTypeId;
    String logoUrl;

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
}
