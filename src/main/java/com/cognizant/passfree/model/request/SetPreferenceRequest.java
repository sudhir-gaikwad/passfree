package com.cognizant.passfree.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetPreferenceRequest {
    
    private String customerId;
    
    private String preferenceId;
    
    private Boolean enabled;
}
