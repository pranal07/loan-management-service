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
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String loanId;
    private String uniqueUserId;
    private String loanType;
    private Double loanAmount;
    private Double interestRate;
    private Integer termPeriod;
    private LocalDate disbursementDate;
    private Double emiAmount;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    @Builder.Default
    private List<EMI> emis = new ArrayList<>();
} 