package com.cognizant.passfree.entities;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PreferenceId implements Serializable {
    
    private String customerId;
    
    private String type;
}
