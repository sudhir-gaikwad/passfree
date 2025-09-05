package com.cognizant.passfree.service;

import com.cognizant.passfree.entities.Preference;
import com.cognizant.passfree.model.response.PreferenceResponse;
import com.cognizant.passfree.repository.PreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PreferenceService {
    
    private static final Logger logger = LoggerFactory.getLogger(PreferenceService.class);
    
    @Autowired
    private PreferenceRepository preferenceRepository;
    
    public List<PreferenceResponse> getPreferencesByCustomerId(String customerId) {
        logger.info("Fetching preferences for customer ID: {}", customerId);
        List<Preference> preferences = preferenceRepository.findByCustomerId(customerId);
        
        return preferences.stream().map(preference -> PreferenceResponse.builder()
                .customerId(preference.getCustomerId())
                .type(preference.getType())
                .preferenceId(preference.getPreferenceId())
                .enabled(preference.getEnabled())
                .data(preference.getData())
                .createdByTs(preference.getCreatedByTs())
                .updatedByTs(preference.getUpdatedByTs())
                .build())
                .collect(Collectors.toList());
    }
    
    public boolean setPreference(String customerId, String preferenceId, Boolean enabled) {
        logger.info("Setting preference for customer ID: {}, preference ID: {}, enabled: {}", customerId, preferenceId, enabled);
        
        Optional<Preference> preferenceOptional = preferenceRepository.findById(preferenceId);
        logger.info("Found preference: {}", preferenceOptional.isPresent());
        
        if (preferenceOptional.isPresent()) {
            Preference preference = preferenceOptional.get();
            logger.info("Preference customer ID: {}, Request customer ID: {}", preference.getCustomerId(), customerId);
            // Verify that the preference belongs to the specified customer
            if (preference.getCustomerId().equals(customerId)) {
                preference.setEnabled(enabled);
                preference.setUpdatedByTs(LocalDateTime.now());
                Preference savedPreference = preferenceRepository.save(preference);
                logger.info("Preference updated successfully. Saved preference ID: {}", savedPreference.getPreferenceId());
                return true;
            } else {
                logger.warn("Preference ID {} does not belong to customer ID {}", preferenceId, customerId);
                return false;
            }
        } else {
            logger.warn("Preference with ID {} not found", preferenceId);
            return false;
        }
    }
}
