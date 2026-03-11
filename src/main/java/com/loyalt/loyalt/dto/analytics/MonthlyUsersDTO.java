package com.loyalt.loyalt.dto.analytics;

public class MonthlyUsersDTO {

    private String month;
    private Long users;

    public MonthlyUsersDTO(String month, Long users) {
        this.month = month;
        this.users = users;
    }

    public String getMonth() {
        return month;
    }

    public Long getUsers() {
        return users;
    }
}