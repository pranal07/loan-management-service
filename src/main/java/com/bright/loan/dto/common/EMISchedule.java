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
public class EMISchedule {
  private LocalDate date;
  private Double amountDue;
}
