package com.cognizant.passfree.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    
    private String sourceAccountNumber;
    private String beneficiaryAccountNumber;
    private BigDecimal amount;
    private String operatingSystem;
    private String state;
    private String country;
}
