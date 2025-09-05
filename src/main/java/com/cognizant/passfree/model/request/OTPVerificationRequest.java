package com.cognizant.passfree.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTPVerificationRequest {
    
    private String transactionId;
    private String otp;
}
