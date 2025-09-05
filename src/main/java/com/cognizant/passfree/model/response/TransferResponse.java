package com.cognizant.passfree.model.response;

import com.cognizant.passfree.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {
    
    private TransactionStatus status;
    private String message;
    private String transactionId;
}
