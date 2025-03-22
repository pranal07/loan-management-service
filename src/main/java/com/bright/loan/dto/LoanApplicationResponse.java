/**
 * @author Pranal
 */
package com.bright.loan.dto;

import com.bright.loan.dto.common.EMISchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponse {
    private String status;
    private String error;
    private String loanId;
    private List<EMISchedule> dueDates;
} 