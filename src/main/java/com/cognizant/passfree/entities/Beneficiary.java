package com.cognizant.passfree.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary {
    
    @Id
    @Column(name = "beneficiary_id", length = 10)
    private String beneficiaryId;
    
    @Column(name = "cust_id", length = 10, nullable = false)
    private String customerId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "acc_num", length = 50, nullable = false)
    private String accountNumber;
    
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id", insertable = false, updatable = false)
    private Customer customer;
}
