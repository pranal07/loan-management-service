/**
 * @author Pranal
 */
package com.bright.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String aadharId;
    private String name;
    private String emailId;
    private Double annualIncome;
}