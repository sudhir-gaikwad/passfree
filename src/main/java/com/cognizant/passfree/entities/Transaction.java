package com.cognizant.passfree.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "cust_id", length = 10, nullable = false)
    private String customerId;
    
    @Column(name = "from_acc_num", length = 50, nullable = false)
    private String fromAccountNumber;
    
    @Column(name = "to_acc_num", length = 50, nullable = false)
    private String toAccountNumber;
    
    @Column(name = "amt_before", precision = 15, scale = 2, nullable = false)
    private BigDecimal amountBefore;
    
    @Column(name = "amt_after", precision = 15, scale = 2, nullable = false)
    private BigDecimal amountAfter;
    
    @Column(name = "transfer_amt", precision = 15, scale = 2, nullable = false)
    private BigDecimal transferAmount;
    
    @Column(name = "status", length = 50, nullable = false)
    private String status;
    
    @Column(name = "mfa_type", length = 50)
    private String mfaType;
    
    @Column(name = "otp", length = 10)
    private String otp;
    
    @Column(name = "created_by_ts")
    private LocalDateTime createdByTs;
    
    @Column(name = "updated_by_ts")
    private LocalDateTime updatedByTs;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id", insertable = false, updatable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_acc_num", referencedColumnName = "acc_num", insertable = false, updatable = false)
    private Account fromAccount;
}
