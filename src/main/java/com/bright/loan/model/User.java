/**
 * @author Pranal
 */
package com.bright.loan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private String aadharId;
    private String uniqueUserId;
    private String name;
    private String email;
    private Double annualIncome;
    private Integer creditScore;
} 