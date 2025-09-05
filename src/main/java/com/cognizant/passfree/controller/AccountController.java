package com.cognizant.passfree.controller;

import com.cognizant.passfree.model.request.TransferRequest;
import com.cognizant.passfree.model.response.TransferResponse;
import com.cognizant.passfree.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Transfer amount from source account to beneficiary account
     * 
     * @param transferRequest the transfer request containing source account, beneficiary account and amount
     * @return ResponseEntity with TransferResponse
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferAmount(@RequestBody TransferRequest transferRequest) {
        
        logger.info("Received transfer request from account {} to account {} with amount {}", 
            transferRequest.getSourceAccountNumber(), transferRequest.getBeneficiaryAccountNumber(), transferRequest.getAmount());
        
        try {
            TransferResponse response = accountService.transferAmount(
                transferRequest.getSourceAccountNumber(),
                transferRequest.getBeneficiaryAccountNumber(),
                transferRequest.getAmount());
            
            if (response.isSuccess()) {
                logger.info("Transfer successful from account {} to account {} with amount {}", 
                    transferRequest.getSourceAccountNumber(), transferRequest.getBeneficiaryAccountNumber(), transferRequest.getAmount());
                return ResponseEntity.ok(response);
            } else {
                logger.error("Transfer failed from account {} to account {} with amount {}: {}", 
                    transferRequest.getSourceAccountNumber(), transferRequest.getBeneficiaryAccountNumber(), transferRequest.getAmount(), response.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("Exception occurred during transfer from account {} to account {} with amount {}: {}", 
                transferRequest.getSourceAccountNumber(), transferRequest.getBeneficiaryAccountNumber(), transferRequest.getAmount(), e.getMessage(), e);
            TransferResponse errorResponse = TransferResponse.builder()
                .success(false)
                .message("Internal server error")
                .build();
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
