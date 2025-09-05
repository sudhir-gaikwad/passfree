package com.cognizant.passfree.service;

import com.cognizant.passfree.entities.Account;
import com.cognizant.passfree.entities.Beneficiary;
import com.cognizant.passfree.entities.Transaction;
import com.cognizant.passfree.model.response.TransferResponse;
import com.cognizant.passfree.repository.AccountRepository;
import com.cognizant.passfree.repository.BeneficiaryRepository;
import com.cognizant.passfree.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    /**
     * Transfer amount from source account to beneficiary account
     * 
     * @param sourceAccountNumber the account number to transfer from
     * @param beneficiaryAccountNumber the account number to transfer to
     * @param amount the amount to transfer
     * @return TransferResponse with success status and message
     */
    @Transactional
    public TransferResponse transferAmount(String sourceAccountNumber, String beneficiaryAccountNumber, BigDecimal amount) {
        logger.info("Initiating transfer from account {} to account {} with amount {}", 
            sourceAccountNumber, beneficiaryAccountNumber, amount);
        
        // Validate input parameters
        if (sourceAccountNumber == null || sourceAccountNumber.isEmpty() || 
            beneficiaryAccountNumber == null || beneficiaryAccountNumber.isEmpty() || 
            amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Invalid input parameters for transfer");
            return TransferResponse.builder()
                .success(false)
                .message("Invalid input parameters for transfer")
                .build();
        }
        
        // Check if source and beneficiary accounts are the same
        if (sourceAccountNumber.equals(beneficiaryAccountNumber)) {
            logger.error("Source and beneficiary accounts cannot be the same");
            return TransferResponse.builder()
                .success(false)
                .message("Source and beneficiary accounts cannot be the same")
                .build();
        }
        
        // Find source account
        Optional<Account> sourceAccountOptional = accountRepository.findById(sourceAccountNumber);
        if (sourceAccountOptional.isEmpty()) {
            logger.error("Source account not found: {}", sourceAccountNumber);
            return TransferResponse.builder()
                .success(false)
                .message("Source account not found")
                .build();
        }
        
        // Find beneficiary by account number
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByAccountNumber(beneficiaryAccountNumber);
        if (beneficiaries.isEmpty()) {
            logger.error("Beneficiary not found for account number: {}", beneficiaryAccountNumber);
            return TransferResponse.builder()
                .success(false)
                .message("Beneficiary not found for account number")
                .build();
        }
        
        // Get the first beneficiary (assuming account number is unique)
        Beneficiary beneficiary = beneficiaries.get(0);
        
        Account sourceAccount = sourceAccountOptional.get();

        // Check if source account has sufficient balance
        if (sourceAccount.getBalanceAmount().compareTo(amount) < 0) {
            logger.error("Insufficient balance in source account. Available: {}, Required: {}", 
                sourceAccount.getBalanceAmount(), amount);
            return TransferResponse.builder()
                .success(false)
                .message("Insufficient balance in source account")
                .build();
        }
        
        // Perform the transfer
        BigDecimal sourceBalanceBefore = sourceAccount.getBalanceAmount();

        // Deduct amount from source account
        sourceAccount.setBalanceAmount(sourceAccount.getBalanceAmount().subtract(amount));
        sourceAccount.setUpdatedByTs(LocalDateTime.now());
        
        // Save updated accounts
        accountRepository.save(sourceAccount);

        // Create and save transaction record
        Transaction transaction = Transaction.builder()
            .customerId(sourceAccount.getCustomer().getCustomerId())
            .fromAccountNumber(sourceAccountNumber)
            .toAccountNumber(beneficiaryAccountNumber)
            .amountBefore(sourceBalanceBefore)
            .amountAfter(sourceAccount.getBalanceAmount())
            .transferAmount(amount)
            .status("SUCCESS")
            .createdByTs(LocalDateTime.now())
            .updatedByTs(LocalDateTime.now())
            .build();
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        logger.info("Transfer completed successfully from account {} to account {} with amount {}", 
            sourceAccountNumber, beneficiaryAccountNumber, amount);
        
        return TransferResponse.builder()
            .success(true)
            .message("Transfer completed successfully")
            .transactionId(String.valueOf(savedTransaction.getId()))
            .build();
    }
}
