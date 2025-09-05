package com.cognizant.passfree.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "preference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PreferenceId.class)
public class Preference {
    
    @Id
    @Column(name = "cust_id", length = 10)
    private String customerId;
    
    @Id
    @Column(name = "type", length = 50)
    private String type;
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
    
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id", insertable = false, updatable = false)
    private Customer customer;
}
