package com.cognizant.passfree.repository;

import com.cognizant.passfree.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByEmailAndPassword(String email, String password);
}
