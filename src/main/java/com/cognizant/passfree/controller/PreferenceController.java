package com.cognizant.passfree.controller;

import com.cognizant.passfree.model.request.SetPreferenceRequest;
import com.cognizant.passfree.model.response.PreferenceResponse;
import com.cognizant.passfree.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = "*")
public class PreferenceController {
    
    @Autowired
    private PreferenceService preferenceService;
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PreferenceResponse>> getPreferences(@PathVariable String customerId) {
        List<PreferenceResponse> preferences = preferenceService.getPreferencesByCustomerId(customerId);
        return ResponseEntity.ok(preferences);
    }
    
    @PutMapping("/customer/preference")
    public ResponseEntity<String> setPreference(
            @RequestBody SetPreferenceRequest request) {
        // Use path variables for consistency, but validate request body if provided
        boolean success = preferenceService.setPreference(request.getCustomerId(), request.getPreferenceId(), request.getEnabled());
        if (success) {
            return ResponseEntity.ok("Preference updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
