package com.cognizant.passfree.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    
    @Id
    @Column(name = "cust_id", length = 10)
    private String customerId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "password", length = 255, nullable = false)
    private String password;
    
    @Column(name = "country", length = 100)
    private String country;
    
    @Column(name = "state", length = 100)
    private String state;
    
    @Column(name = "city", length = 100)
    private String city;
    
    @Column(name = "zip", length = 20)
    private String zip;
    
    @Column(name = "created_by_ts")
    private LocalDateTime createdByTs;
    
    @Column(name = "updated_by_ts")
    private LocalDateTime updatedByTs;
    
    // Relationships
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "acc_num", referencedColumnName = "acc_num", nullable = false, updatable = false)
    private Account account;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Preference> preferences;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Beneficiary> beneficiaries;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
