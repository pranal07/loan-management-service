package com.bright.loan.dto.common;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PastTransaction {
  private LocalDate date;
  private Double principal;
  private Double interest;
  private Double amountPaid;
}
