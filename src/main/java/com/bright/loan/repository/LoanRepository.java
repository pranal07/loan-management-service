/**
 * @author Pranal
 */
package com.bright.loan.repository;

import com.bright.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {
    List<Loan> findByUniqueUserId(String uniqueUserId);
} 