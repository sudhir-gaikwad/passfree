package com.cognizant.passfree.service;

import com.cognizant.passfree.entities.Customer;
import com.cognizant.passfree.model.request.TransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class RuleEngineService {
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * Evaluate if OTP authentication is required based on transaction rules
     * 
     * @param transferRequest the transfer request containing transaction details
     * @param customer the customer initiating the transaction
     * @return true if OTP authentication is required, false otherwise
     */
    public boolean isOTPRequired(TransferRequest transferRequest, Customer customer) {
        // Rule 1: If transaction amount is greater than 100 then OTP based authentication will be used
        if (transferRequest.getAmount() != null && transferRequest.getAmount().compareTo(new BigDecimal("100")) > 0) {
            return true;
        }
        
        // Rule 2: If State or country sent in transaction API is different that customers state and country then OTP based authentication will be used
        if (isLocationDifferent(transferRequest, customer)) {
            return true;
        }
        
        // If none of the rules apply, OTP is not required
        return false;
    }
    
    /**
     * Check if the location in the transfer request is different from the customer's location
     * 
     * @param transferRequest the transfer request containing transaction details
     * @param customer the customer initiating the transaction
     * @return true if location is different, false otherwise
     */
    private boolean isLocationDifferent(TransferRequest transferRequest, Customer customer) {
        // Check if state is provided in request and is different from customer's state
        if (transferRequest.getState() != null && !transferRequest.getState().isEmpty()) {
            if (customer.getState() == null || !transferRequest.getState().equals(customer.getState())) {
                return true;
            }
        }
        
        // Check if country is provided in request and is different from customer's country
        if (transferRequest.getCountry() != null && !transferRequest.getCountry().isEmpty()) {
            if (customer.getCountry() == null || !transferRequest.getCountry().equals(customer.getCountry())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Generate a 6-digit OTP
     * 
     * @return a 6-digit OTP as a string
     */
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate a number between 100000 and 999999
        return String.valueOf(otp);
    }
}
