package com.cognizant.passfree.repository;

import com.cognizant.passfree.entities.Preference;
import com.cognizant.passfree.entities.PreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, PreferenceId> {
}
