package com.loyalt.loyalt.dto.business;

import java.util.UUID;

public record CreateBusinessResponse (UUID businessId, String businessName, String programName, String logoUrl){

}
