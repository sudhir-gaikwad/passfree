package com.cognizant.passfree.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceResponse {
    
    private String customerId;
    
    private String type;
    
    private String preferenceId;
    
    private Boolean enabled;
    
    private String data;
    
    private LocalDateTime createdByTs;
    
    private LocalDateTime updatedByTs;
}
