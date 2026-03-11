package com.loyalt.loyalt.repository;

import com.loyalt.loyalt.model.entity.CustomerBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerBusinessRepository extends JpaRepository<CustomerBusiness, UUID> {

    Optional<CustomerBusiness> findByCustomerUuidAndBusinessUuid(UUID customerId, UUID businessId);

    // Query nativa OK, solo pasa el businessId como parámetro
    @Query(value = """
        SELECT 
            TO_CHAR(month, 'YYYY-MM') AS month,
            COALESCE(COUNT(cb.uuid), 0) AS users
        FROM generate_series(
            DATE_TRUNC('month', NOW()) - INTERVAL '5 months',
            DATE_TRUNC('month', NOW()),
            INTERVAL '1 month'
        ) AS month
        LEFT JOIN customer_business cb
            ON DATE_TRUNC('month', cb.joined_at) = month
            AND cb.business_id = :businessId
        GROUP BY month
        ORDER BY month
        """, nativeQuery = true)
    List<Object[]> countUsersLastSixMonthsByBusiness(@Param("businessId") UUID businessId);

    // CORREGIDO: JPQL usa cb.business.uuid
    @Query("SELECT COUNT(cb) FROM CustomerBusiness cb WHERE cb.business.uuid = :businessId")
    Long countTotalUsersByBusiness(@Param("businessId") UUID businessId);

    @Query("""
        SELECT COUNT(cb)
        FROM CustomerBusiness cb
        WHERE cb.joinedAt >= DATE_TRUNC('month', CURRENT_TIMESTAMP)
          AND cb.business.uuid = :businessId
    """)
    Long countUsersThisMonthByBusiness(@Param("businessId") UUID businessId);

    @Query("""
        SELECT COUNT(cb)
        FROM CustomerBusiness cb
        WHERE cb.status = 'ACTIVE'
          AND cb.business.uuid = :businessId
    """)
    Long countActiveUsersByBusiness(@Param("businessId") UUID businessId);
}