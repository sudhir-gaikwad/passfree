package com.cognizant.passfree.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    
    @Column(name = "cust_id", length = 10, nullable = false)
    private String customerId;
    
    @Column(name = "balance_amt", precision = 15, scale = 2, nullable = false)
    private BigDecimal balanceAmount;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id", insertable = false, updatable = false)
    private Customer customer;
    
    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
