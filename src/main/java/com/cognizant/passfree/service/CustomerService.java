package com.cognizant.passfree.service;

import com.cognizant.passfree.entities.Customer;
import com.cognizant.passfree.model.request.LoginRequest;
import com.cognizant.passfree.model.response.LoginResponse;
import com.cognizant.passfree.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
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
}
