package com.cognizant.passfree.model.response;

import com.cognizant.passfree.model.MfaType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceResponse {
    
    private String customerId;
    
    private MfaType type;
    
    private String preferenceId;
    
    private Boolean enabled;
    
    private String data;
    
    private LocalDateTime createdByTs;
    
    private LocalDateTime updatedByTs;
}
