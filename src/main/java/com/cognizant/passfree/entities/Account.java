package com.cognizant.passfree.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    
    @Id
    @Column(name = "acc_num", length = 50)
    private String accountNumber;

    @Column(name = "balance_amt", precision = 15, scale = 2, nullable = false)
    private BigDecimal balanceAmount;
    
    @Column(name = "created_by_ts")
    private LocalDateTime createdByTs;
    
    @Column(name = "updated_by_ts")
    private LocalDateTime updatedByTs;
    
    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id")
    private Customer customer;
    
    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
