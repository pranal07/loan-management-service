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
public class UserRegistrationResponse {
    private String status;
    private String error;
    private String uniqueUserId;
    private Integer creditScore;
} 