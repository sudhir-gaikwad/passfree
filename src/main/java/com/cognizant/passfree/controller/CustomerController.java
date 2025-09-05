package com.cognizant.passfree.controller;

import com.cognizant.passfree.model.request.LoginRequest;
import com.cognizant.passfree.model.response.LoginResponse;
import com.cognizant.passfree.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = customerService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
