/**
 * @author Pranal
 */
package com.bright.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.bright.loan.model")
@EnableJpaRepositories("com.bright.loan.repository")
public class LoanApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoanApplication.class, args);
        System.out.println("Loan Management System Started Successfully!!!");
    }
} 