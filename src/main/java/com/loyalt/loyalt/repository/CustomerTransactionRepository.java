package com.loyalt.loyalt.repository;

import com.loyalt.loyalt.model.entity.CustomerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, UUID> {

}
