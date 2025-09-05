package com.cognizant.passfree.model.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    
    private String accountNumber;
    
    private BigDecimal balanceAmount;
    
    private LocalDateTime createdByTs;
    
    private LocalDateTime updatedByTs;
    
    private String customerId;
    
    private String customerName;
}
