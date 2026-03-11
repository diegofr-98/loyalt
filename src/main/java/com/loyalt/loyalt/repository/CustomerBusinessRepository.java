package com.loyalt.loyalt.repository;

import com.loyalt.loyalt.dto.business.BusinessCustomerDTO;
import com.loyalt.loyalt.model.entity.CustomerBusiness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = """
        SELECT
            cb.uuid as id,
            cb.google_object_id as googleObjectId,
            c.email as email,
            c.phone_number as phone,
            cb.points_balance as points,
            c.created_at as createdAt
        FROM customer_business cb
        JOIN customer c ON c.uuid = cb.customer_id
        WHERE cb.business_id = :businessId
        ORDER BY c.created_at DESC
        """,
            countQuery = """
        SELECT count(*)
        FROM customer_business cb
        WHERE cb.business_id = :businessId
        """,
            nativeQuery = true)
    Page<BusinessCustomerDTO> findCustomersByBusinessId(
            @Param("businessId") UUID businessId,
            Pageable pageable
    );
}