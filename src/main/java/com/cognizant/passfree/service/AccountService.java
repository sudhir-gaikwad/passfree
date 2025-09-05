package com.cognizant.passfree.service;

import com.cognizant.passfree.entities.Account;
import com.cognizant.passfree.entities.Beneficiary;
import com.cognizant.passfree.entities.Customer;
import com.cognizant.passfree.entities.Transaction;
import com.cognizant.passfree.model.MfaType;
import com.cognizant.passfree.model.TransactionStatus;
import com.cognizant.passfree.model.request.TransferRequest;
import com.cognizant.passfree.model.response.AccountResponse;
import com.cognizant.passfree.model.response.TransferResponse;
import com.cognizant.passfree.repository.AccountRepository;
import com.cognizant.passfree.repository.BeneficiaryRepository;
import com.cognizant.passfree.repository.CustomerRepository;
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
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private RuleEngineService ruleEngineService;
    
    /**
     * Get account details by account number
     * 
     * @param accountNumber the account number to retrieve details for
     * @return AccountResponse with account details
     */
    public AccountResponse getAccountDetails(String accountNumber) {
        logger.info("Fetching account details for account number: {}", accountNumber);
        
        // Validate input parameter
        if (accountNumber == null || accountNumber.isEmpty()) {
            logger.error("Invalid account number provided");
            return null;
        }
        
        // Find account by account number
        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
        if (accountOptional.isEmpty()) {
            logger.error("Account not found for account number: {}", accountNumber);
            return null;
        }
        
        Account account = accountOptional.get();
        
        // Build response object
        AccountResponse accountResponse = AccountResponse.builder()
            .accountNumber(account.getAccountNumber())
            .balanceAmount(account.getBalanceAmount())
            .createdByTs(account.getCreatedByTs())
            .updatedByTs(account.getUpdatedByTs())
            .build();
            
        // Set customer details if available
        if (account.getCustomer() != null) {
            accountResponse.setCustomerId(account.getCustomer().getCustomerId());
            accountResponse.setCustomerName(account.getCustomer().getName());
        }
        
        logger.info("Successfully retrieved account details for account number: {}", accountNumber);
        return accountResponse;
    }
    
    /**
     * Transfer amount from source account to beneficiary account
     * 
     * @param transferRequest the transfer request containing all transaction details
     * @return TransferResponse with success status and message
     */
    @Transactional
    public TransferResponse transferAmount(TransferRequest transferRequest) {
        logger.info("Initiating transfer from account {} to account {} with amount {}. OS: {}, State: {}, Country: {}", 
            transferRequest.getSourceAccountNumber(), transferRequest.getBeneficiaryAccountNumber(), transferRequest.getAmount(),
            transferRequest.getOperatingSystem(), transferRequest.getState(), transferRequest.getCountry());
        
        // Validate input parameters
        if (transferRequest.getSourceAccountNumber() == null || transferRequest.getSourceAccountNumber().isEmpty() || 
            transferRequest.getBeneficiaryAccountNumber() == null || transferRequest.getBeneficiaryAccountNumber().isEmpty() || 
            transferRequest.getAmount() == null || transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Invalid input parameters for transfer");
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Invalid input parameters for transfer")
                .build();
        }
        
        // Check if source and beneficiary accounts are the same
        if (transferRequest.getSourceAccountNumber().equals(transferRequest.getBeneficiaryAccountNumber())) {
            logger.error("Source and beneficiary accounts cannot be the same");
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Source and beneficiary accounts cannot be the same")
                .build();
        }
        
        // Find source account
        Optional<Account> sourceAccountOptional = accountRepository.findById(transferRequest.getSourceAccountNumber());
        if (sourceAccountOptional.isEmpty()) {
            logger.error("Source account not found: {}", transferRequest.getSourceAccountNumber());
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Source account not found")
                .build();
        }
        
        // Find beneficiary by account number
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByAccountNumber(transferRequest.getBeneficiaryAccountNumber());
        if (beneficiaries.isEmpty()) {
            logger.error("Beneficiary not found for account number: {}", transferRequest.getBeneficiaryAccountNumber());
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Beneficiary not found for account number")
                .build();
        }
        
        // Get the first beneficiary (assuming account number is unique)
        Beneficiary beneficiary = beneficiaries.get(0);
        
        Account sourceAccount = sourceAccountOptional.get();
        Customer customer = sourceAccount.getCustomer();

        // Check if OTP is required based on rules
        boolean isOTPRequired = ruleEngineService.isOTPRequired(transferRequest, customer);
        
        if (isOTPRequired) {
            // OTP is required - create transaction in INITIATED status
            String otp = ruleEngineService.generateOTP();
            
            // Create and save transaction record with OTP
            Transaction transaction = Transaction.builder()
                .customerId(customer.getCustomerId())
                .fromAccountNumber(transferRequest.getSourceAccountNumber())
                .toAccountNumber(transferRequest.getBeneficiaryAccountNumber())
                .amountBefore(sourceAccount.getBalanceAmount()) // No deduction yet
                .amountAfter(sourceAccount.getBalanceAmount())  // No deduction yet
                .transferAmount(transferRequest.getAmount())
                .status(TransactionStatus.INITIATED)
                .otp(otp)
                .mfaType(MfaType.SMS)
                .createdByTs(LocalDateTime.now())
                .updatedByTs(LocalDateTime.now())
                .build();
            
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            logger.info("Transfer initiated with OTP authentication. Transaction ID: {}, OTP: {}", 
                savedTransaction.getId(), otp);
            
            return TransferResponse.builder()
                .status(TransactionStatus.INITIATED)
                .message("OTP required for this transaction. Please verify OTP to complete transfer.")
                .transactionId(String.valueOf(savedTransaction.getId()))
                .otp(otp) // In a real implementation, this would be sent via email/SMS
                .build();
        } else {
            // No OTP required - proceed with normal transfer
            // Check if source account has sufficient balance
            if (sourceAccount.getBalanceAmount().compareTo(transferRequest.getAmount()) < 0) {
                logger.error("Insufficient balance in source account. Available: {}, Required: {}", 
                    sourceAccount.getBalanceAmount(), transferRequest.getAmount());
                return TransferResponse.builder()
                    .status(TransactionStatus.FAILED)
                    .message("Insufficient balance in source account")
                    .build();
            }
            
            // Perform the transfer
            BigDecimal sourceBalanceBefore = sourceAccount.getBalanceAmount();

            // Deduct amount from source account
            sourceAccount.setBalanceAmount(sourceAccount.getBalanceAmount().subtract(transferRequest.getAmount()));
            sourceAccount.setUpdatedByTs(LocalDateTime.now());
            
            // Save updated accounts
            accountRepository.save(sourceAccount);

            // Create and save transaction record
            Transaction transaction = Transaction.builder()
                .customerId(customer.getCustomerId())
                .fromAccountNumber(transferRequest.getSourceAccountNumber())
                .toAccountNumber(transferRequest.getBeneficiaryAccountNumber())
                .amountBefore(sourceBalanceBefore)
                .amountAfter(sourceAccount.getBalanceAmount())
                .transferAmount(transferRequest.getAmount())
                .status(TransactionStatus.SUCCESS)
                .createdByTs(LocalDateTime.now())
                .updatedByTs(LocalDateTime.now())
                .build();
            
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            logger.info("Transfer completed successfully from account {} to account {} with amount {}", 
                transferRequest.getSourceAccountNumber(), transferRequest.getBeneficiaryAccountNumber(), transferRequest.getAmount());
            
            return TransferResponse.builder()
                .status(transaction.getStatus())
                .message("Transfer completed successfully")
                .transactionId(String.valueOf(savedTransaction.getId()))
                .build();
        }
    }
    
    /**
     * Verify OTP and complete the transaction
     * 
     * @param transactionId the transaction ID
     * @param otp the OTP provided by the user
     * @return TransferResponse with success status and message
     */
    @Transactional
    public TransferResponse verifyOTPAndCompleteTransaction(String transactionId, String otp) {
        logger.info("Verifying OTP for transaction ID: {}", transactionId);
        
        // Validate input parameters
        if (transactionId == null || transactionId.isEmpty() || otp == null || otp.isEmpty()) {
            logger.error("Invalid input parameters for OTP verification");
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Invalid input parameters for OTP verification")
                .build();
        }
        
        // Find transaction by ID
        Optional<Transaction> transactionOptional = transactionRepository.findById(Long.valueOf(transactionId));
        if (transactionOptional.isEmpty()) {
            logger.error("Transaction not found: {}", transactionId);
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Transaction not found")
                .build();
        }
        
        Transaction transaction = transactionOptional.get();
        
        // Check if transaction is in INITIATED status
        if (transaction.getStatus() != TransactionStatus.INITIATED) {
            logger.error("Transaction is not in INITIATED status: {}", transactionId);
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Transaction is not in a valid state for OTP verification")
                .build();
        }
        
        // Verify OTP
        if (!otp.equals(transaction.getOtp())) {
            logger.error("Invalid OTP provided for transaction: {}", transactionId);
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Invalid OTP provided")
                .build();
        }
        
        // OTP is valid, proceed with the transfer
        // Find source account
        Optional<Account> sourceAccountOptional = accountRepository.findById(transaction.getFromAccountNumber());
        if (sourceAccountOptional.isEmpty()) {
            logger.error("Source account not found: {}", transaction.getFromAccountNumber());
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Source account not found")
                .build();
        }
        
        Account sourceAccount = sourceAccountOptional.get();
        
        // Check if source account has sufficient balance
        if (sourceAccount.getBalanceAmount().compareTo(transaction.getTransferAmount()) < 0) {
            logger.error("Insufficient balance in source account. Available: {}, Required: {}", 
                sourceAccount.getBalanceAmount(), transaction.getTransferAmount());
            // Update transaction status to FAILED
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setUpdatedByTs(LocalDateTime.now());
            transactionRepository.save(transaction);
            
            return TransferResponse.builder()
                .status(TransactionStatus.FAILED)
                .message("Insufficient balance in source account")
                .build();
        }
        
        // Perform the transfer
        BigDecimal sourceBalanceBefore = sourceAccount.getBalanceAmount();
        
        // Deduct amount from source account
        sourceAccount.setBalanceAmount(sourceAccount.getBalanceAmount().subtract(transaction.getTransferAmount()));
        sourceAccount.setUpdatedByTs(LocalDateTime.now());
        
        // Save updated account
        accountRepository.save(sourceAccount);
        
        // Update transaction status to SUCCESS
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAmountBefore(sourceBalanceBefore);
        transaction.setAmountAfter(sourceAccount.getBalanceAmount());
        transaction.setUpdatedByTs(LocalDateTime.now());
        transactionRepository.save(transaction);
        
        logger.info("Transfer completed successfully for transaction ID: {}", transactionId);
        
        return TransferResponse.builder()
            .status(TransactionStatus.SUCCESS)
            .message("Transfer completed successfully")
            .transactionId(transactionId)
            .build();
    }
}
