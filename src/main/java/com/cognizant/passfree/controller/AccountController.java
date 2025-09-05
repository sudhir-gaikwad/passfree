package com.cognizant.passfree.controller;

import com.cognizant.passfree.model.TransactionStatus;
import com.cognizant.passfree.model.request.TransferRequest;
import com.cognizant.passfree.model.response.AccountResponse;
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
     * Get account details by account number
     * 
     * @param accountNumber the account number to retrieve details for
     * @return ResponseEntity with AccountResponse
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountDetails(@PathVariable String accountNumber) {
        logger.info("Received request for account details for account number: {}", accountNumber);
        
        try {
            AccountResponse response = accountService.getAccountDetails(accountNumber);
            
            if (response != null) {
                logger.info("Successfully retrieved account details for account number: {}", accountNumber);
                return ResponseEntity.ok(response);
            } else {
                logger.error("Account not found for account number: {}", accountNumber);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Exception occurred while fetching account details for account number {}: {}", 
                accountNumber, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Transfer amount from source account to beneficiary account
     * 
     * @param transferRequest the transfer request containing source account, beneficiary account, amount, and additional details
     * @return ResponseEntity with TransferResponse
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferAmount(@RequestBody TransferRequest transferRequest) {
        
        logger.info("Received transfer request from account {} to account {} with amount {}. OS: {}, State: {}, Country: {}", 
            transferRequest.getSourceAccountNumber(), transferRequest.getBeneficiaryAccountNumber(), transferRequest.getAmount(),
            transferRequest.getOperatingSystem(), transferRequest.getState(), transferRequest.getCountry());
        
        try {
            TransferResponse response = accountService.transferAmount(
                transferRequest.getSourceAccountNumber(),
                transferRequest.getBeneficiaryAccountNumber(),
                transferRequest.getAmount(),
                transferRequest.getOperatingSystem(),
                transferRequest.getState(),
                transferRequest.getCountry());
            
            if (response.getStatus() == TransactionStatus.SUCCESS) {
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
                .status(TransactionStatus.FAILED)
                .message("Internal server error")
                .build();
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
