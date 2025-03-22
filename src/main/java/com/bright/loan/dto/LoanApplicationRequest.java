/**
 * @author Pranal
 */
package com.bright.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationRequest {
    private String uniqueUserId;
    private String loanType;
    private Double loanAmount;
    private Double interestRate;
    private Integer termPeriod;
    private LocalDate disbursementDate;
} 