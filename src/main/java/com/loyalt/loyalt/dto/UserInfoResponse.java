package com.loyalt.loyalt.dto;

import com.loyalt.loyalt.model.enums.Role;

import java.util.UUID;

public record UserInfoResponse(UUID id, String email, Role role) {

}
