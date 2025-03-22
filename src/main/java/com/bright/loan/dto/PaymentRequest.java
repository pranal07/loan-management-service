/**
 * @author Pranal
 */
package com.bright.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String loanId;
    private Double amount;
    private LocalDate paymentDate;

} 