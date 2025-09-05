package com.cognizant.passfree.controller;

import com.cognizant.passfree.model.request.TransferRequest;
import com.cognizant.passfree.model.response.TransferResponse;
import com.cognizant.passfree.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Test endpoint to verify transfer functionality
     * 
     * @return TransferResponse
     */
    @PostMapping("/transfer")
    public TransferResponse testTransfer() {
        // Create a test transfer request using actual existing accounts
        TransferRequest request = TransferRequest.builder()
            .sourceAccountNumber("ACC001")
            .beneficiaryAccountNumber("BEN_ACC001")
            .amount(new BigDecimal("50.00"))
            .build();
        
        // Execute transfer
        return accountService.transferAmount(
            request.getSourceAccountNumber(),
            request.getBeneficiaryAccountNumber(),
            request.getAmount());
    }
}
