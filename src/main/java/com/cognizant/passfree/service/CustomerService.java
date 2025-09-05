package com.cognizant.passfree.service;

import com.cognizant.passfree.entities.Customer;
import com.cognizant.passfree.model.request.LoginRequest;
import com.cognizant.passfree.model.response.CustomerDetailsResponse;
import com.cognizant.passfree.model.response.LoginResponse;
import com.cognizant.passfree.repository.AccountRepository;
import com.cognizant.passfree.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    public LoginResponse login(LoginRequest loginRequest) {
        // Find customer by email and password
        Optional<Customer> customerOptional = customerRepository.findByEmailAndPassword(
            loginRequest.getEmail(), loginRequest.getPassword());
        
        // Check if customer exists with the provided credentials
        if (customerOptional.isEmpty()) {
            return LoginResponse.builder()
                    .message("Invalid email or password")
                    .build();
        }
        
        Customer customer = customerOptional.get();
        
        // Return successful login response with customer ID
        return LoginResponse.builder()
                .customerId(customer.getCustomerId())
                .message("Login successful")
                .build();
    }
    
    public Optional<CustomerDetailsResponse> getCustomerDetailsById(String customerId) {
        logger.info("Fetching customer details for ID: {}", customerId);
        Optional<Customer> customerOptional = customerRepository.findByCustomerId(customerId);
        
        if (customerOptional.isEmpty()) {
            logger.info("Customer not found for ID: {}", customerId);
            return Optional.empty();
        }
        
        Customer customer = customerOptional.get();
        logger.info("Customer found: {}", customer.getName());
        
        // Fetch account number from the customer's account relationship
        String accountNumber = null;
        if (customer.getAccount() != null) {
            accountNumber = customer.getAccount().getAccountNumber();
            logger.info("Account found via relationship: {}", accountNumber);
        } else {
            logger.info("No account found via relationship, trying fallback method");
        }
        
        CustomerDetailsResponse customerDetailsResponse = CustomerDetailsResponse.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .country(customer.getCountry())
                .state(customer.getState())
                .city(customer.getCity())
                .zip(customer.getZip())
                .createdByTs(customer.getCreatedByTs())
                .updatedByTs(customer.getUpdatedByTs())
                .accountNumber(accountNumber)
                .build();
        
        logger.info("Returning customer details with account number: {}", accountNumber);
        return Optional.of(customerDetailsResponse);
    }
}
