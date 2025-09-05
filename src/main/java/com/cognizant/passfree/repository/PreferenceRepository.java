package com.cognizant.passfree.repository;

import com.cognizant.passfree.entities.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, String> {
    List<Preference> findByCustomerId(String customerId);
}
