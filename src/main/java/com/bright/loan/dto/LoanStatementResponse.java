/**
 * @author Pranal
 */
package com.bright.loan.dto;

import com.bright.loan.dto.common.PastTransaction;
import com.bright.loan.dto.common.UpcomingTransaction;
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
public class LoanStatementResponse {
    private String status;
    private String error;
    private List<PastTransaction> pastTransactions;
    private List<UpcomingTransaction> upcomingTransactions;
}