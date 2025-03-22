package com.bright.loan.service;

import com.bright.loan.constant.AppConstants;
import com.bright.loan.model.EMI;
import com.bright.loan.model.Loan;
import com.bright.loan.model.User;
import com.bright.loan.repository.LoanRepository;
import com.bright.loan.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

@Service
@Slf4j
public class LoanService {

    private static final Map<String, Double> LOAN_LIMITS = new HashMap<>();
    static {
        LOAN_LIMITS.put("Home", 8500000.0);
        LOAN_LIMITS.put("Personal", 100000.0);
        LOAN_LIMITS.put("Car", 750000.0);
        LOAN_LIMITS.put("Educational", 5000000.0);
    }

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LoanRepository loanRepository;

    public Loan createLoan(String uniqueUserId, String loanType, Double loanAmount,
        Double interestRate, Integer termPeriod, LocalDate disbursementDate) {
        User user = userRepository.findByUniqueUserId(uniqueUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        validateLoanApplication(user, loanType, loanAmount, interestRate);

        Loan loan = Loan.builder()
            .uniqueUserId(uniqueUserId)
            .loanType(loanType)
            .loanAmount(loanAmount)
            .interestRate(interestRate)
            .termPeriod(termPeriod)
            .disbursementDate(disbursementDate)
            .build();

        double emiAmount = calculateEMI(loanAmount, interestRate, termPeriod);
        loan.setEmiAmount(emiAmount);

        generateEMISchedule(loan);
        log.info("Loan created Successfully for loanType {} and amount {} with interest rate {}",loanType,loanAmount,interestRate);

        return loanRepository.save(loan);
    }

    private void validateLoanApplication(User user, String loanType, Double loanAmount,
        Double interestRate) {
        if (user.getCreditScore() < 450) {
            throw new RuntimeException("Credit score too low");
        }
        if (user.getAnnualIncome() < 150000) {
            throw new RuntimeException("Annual income too low");
        }
        if (!LOAN_LIMITS.containsKey(loanType) || loanAmount > LOAN_LIMITS.get(loanType)) {
            throw new RuntimeException("Invalid loan type or amount exceeds limit");
        }
        if (interestRate < 14.0) {
            throw new RuntimeException("Interest rate must be at least 14%");
        }

        double monthlyIncome = user.getAnnualIncome() / AppConstants.TOTAL_MONTHS;
        double emiAmount = calculateEMI(loanAmount, interestRate,
            AppConstants.TOTAL_MONTHS); // Calculate for a year
        if (emiAmount > monthlyIncome * 0.6) {
            throw new RuntimeException("EMI amount exceeds 60% of monthly income");
        }
    }

    private double calculateEMI(double principal, double interestRate, int termPeriod) {
        double monthlyRate = (interestRate / AppConstants.TOTAL_MONTHS) / 100;
        return (principal * monthlyRate * Math.pow(1 + monthlyRate, termPeriod)) /
            (Math.pow(1 + monthlyRate, termPeriod) - 1);
    }

    private void generateEMISchedule(Loan loan) {
        LocalDate emiDate = loan.getDisbursementDate().plusMonths(1).withDayOfMonth(1);
        double remainingAmount = loan.getLoanAmount();
        double monthlyRate = (loan.getInterestRate() / AppConstants.TOTAL_MONTHS) / 100;

        for (int i = 0; i < loan.getTermPeriod(); i++) {
            EMI emi = new EMI();
            emi.setLoan(loan);
            emi.setDueDate(emiDate);

            double interest = remainingAmount * monthlyRate;
            double principal = loan.getEmiAmount() - interest;

            // Adjust last EMI if needed
            if (i == loan.getTermPeriod() - 1) {
                principal = remainingAmount;
                emi.setAmountDue(principal + interest);
            } else {
                emi.setAmountDue(loan.getEmiAmount());
            }

            emi.setPrincipalComponent(principal);
            emi.setInterestComponent(interest);

            loan.getEmis().add(emi);

            remainingAmount -= principal;
            emiDate = emiDate.plusMonths(1);
        }
    }
} 