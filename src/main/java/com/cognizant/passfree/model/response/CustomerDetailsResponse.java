package com.cognizant.passfree.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDetailsResponse {
    
    private String customerId;
    
    private String name;
    
    private String email;
    
    private String phone;
    
    private String country;
    
    private String state;
    
    private String city;
    
    private String zip;
    
    private LocalDateTime createdByTs;
    
    private LocalDateTime updatedByTs;
    
    private String accountNumber;
}
