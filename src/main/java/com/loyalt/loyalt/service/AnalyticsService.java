package com.loyalt.loyalt.service;

import com.loyalt.loyalt.dto.analytics.MonthlyUsersDTO;
import com.loyalt.loyalt.dto.analytics.CustomerBusinessAnalyticsDTO;
import com.loyalt.loyalt.repository.CustomerBusinessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnalyticsService {

    private final CustomerBusinessRepository repository;

    public AnalyticsService(CustomerBusinessRepository repository) {
        this.repository = repository;
    }

    public CustomerBusinessAnalyticsDTO getCustomerBusinessAnalytics(UUID businessId) {

        List<MonthlyUsersDTO> monthlyUsers = repository.countUsersLastSixMonthsByBusiness(businessId)
                .stream()
                .map(r -> new MonthlyUsersDTO(
                        (String) r[0],
                        ((Number) r[1]).longValue()
                ))
                .toList();

        Long totalUsers = repository.countTotalUsersByBusiness(businessId);
        Long activeUsers = repository.countActiveUsersByBusiness(businessId);
        Long newUsersThisMonth = repository.countUsersThisMonthByBusiness(businessId);

        return new CustomerBusinessAnalyticsDTO(
                monthlyUsers,
                totalUsers,
                activeUsers,
                newUsersThisMonth
        );
    }
}