package com.loyalt.loyalt.dto;

public class AuthResponse {
    private String token;
    private String role;
    private String type;
    private Long expiresIn;

    private UserInfoResponse userInfoResponse;

    public AuthResponse(String token, String role, String type, Long expiresIn, UserInfoResponse userInfoResponse){
        this.token = token;
        this.role = role;
        this.expiresIn = expiresIn;
        this.userInfoResponse = userInfoResponse;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public UserInfoResponse getUserInfoResponse() {
        return userInfoResponse;
    }

    public String getType() {
        return type;
    }
}
