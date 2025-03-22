/**
 * @author Pranal
 */
package com.bright.loan.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String aadharId;
    private LocalDate date;
    private Double amount;
    private String transactionType;

}