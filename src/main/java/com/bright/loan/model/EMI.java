/**
 * @author Pranal
 */
package com.bright.loan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emis")
public class EMI {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
    
    private LocalDate dueDate;
    private Double amountDue;
    private Double principalComponent;
    private Double interestComponent;
    
    @Builder.Default
    private Boolean isPaid = false;
} 