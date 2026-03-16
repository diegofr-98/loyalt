package com.loyalt.loyalt.dto.business;

import java.time.Instant;

public interface BusinessCustomerDTO {

    String getId();

    String getCustomerId();

    String getGoogleObjectId();

    String getEmail();

    String getPhone();

    Integer getPoints();

    Instant getCreatedAt();
}
