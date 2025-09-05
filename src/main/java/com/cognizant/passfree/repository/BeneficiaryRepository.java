package com.cognizant.passfree.repository;

import com.cognizant.passfree.entities.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, String> {
    List<Beneficiary> findByCustomerId(String customerId);

    List<Beneficiary> findByAccountNumber(String accountNumber);
}
