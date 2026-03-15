package com.loyalt.loyalt.dto.business;


import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record CreateBusinessRequest (
        @NotEmpty(message = "Business Name is required") String businessName,
        @NotEmpty(message = "Business Type ID is required")  String businessTypeId,
        @NotEmpty(message = "Logo Url is required") String logoUrl){

}
