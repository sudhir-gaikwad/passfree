package com.cognizant.passfree.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "preference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preference {
    
    @Column(name = "cust_id", length = 10)
    private String customerId;
    
    @Column(name = "type", length = 50)
    private String type;
    
    @Id
    @Column(name = "preference_id", length = 10)
    private String preferenceId;
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
    
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;
    
    @Column(name = "created_by_ts")
    private LocalDateTime createdByTs;
    
    @Column(name = "updated_by_ts")
    private LocalDateTime updatedByTs;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id", insertable = false, updatable = false)
    private Customer customer;
}
