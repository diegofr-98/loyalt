package com.loyalt.loyalt.dto.analytics;

import java.util.List;

public class CustomerBusinessAnalyticsDTO {
    private List<MonthlyUsersDTO> usersPerMonth;
    private Long totalUsers;
    private Long activeUsers;
    private Long newUsersThisMonth;

    public CustomerBusinessAnalyticsDTO(
            List<MonthlyUsersDTO> usersPerMonth,
            Long totalUsers,
            Long activeUsers,
            Long newUsersThisMonth
    ) {
        this.usersPerMonth = usersPerMonth;
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.newUsersThisMonth = newUsersThisMonth;
    }

    public List<MonthlyUsersDTO> getUsersPerMonth() {
        return usersPerMonth;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public Long getActiveUsers() {
        return activeUsers;
    }

    public Long getNewUsersThisMonth() {
        return newUsersThisMonth;
    }
}
