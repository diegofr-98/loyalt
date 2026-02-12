package com.loyalt.loyalt.repository;

import com.loyalt.loyalt.model.entity.UserBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserBusinessRepository extends JpaRepository<UserBusiness, UUID> {
}
