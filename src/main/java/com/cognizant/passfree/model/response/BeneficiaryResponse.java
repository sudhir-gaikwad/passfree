package com.cognizant.passfree.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryResponse {
    
    private String beneficiaryId;
    
    private String customerId;
    
    private String name;
    
    private String accountNumber;
    
    private String country;
    
    private String state;
    
    private String city;
    
    private String zip;
    
    private LocalDateTime createdByTs;
    
    private LocalDateTime updatedByTs;
}
