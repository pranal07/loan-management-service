/**
 * @author Pranal
 */
package com.bright.loan.controller;

import com.bright.loan.dto.*;
import com.bright.loan.dto.common.EMISchedule;
import com.bright.loan.dto.common.PastTransaction;
import com.bright.loan.dto.common.UpcomingTransaction;
import com.bright.loan.model.*;
import com.bright.loan.service.*;
import com.bright.loan.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/api")
@Slf4j
public class LoanController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditScoreService creditScoreService;

    @Autowired
    private LoanService loanService;

    @PostMapping("/register-user")
    public ResponseEntity<UserRegistrationResponse> registerUser(
        @RequestBody UserRegistrationRequest request) {
        try {
            User user = User.builder()
                .aadharId(request.getAadharId())
                .uniqueUserId(UUID.randomUUID().toString())
                .email(request.getEmailId())
                .annualIncome(request.getAnnualIncome())
                .name(request.getName())
                .build();

            userRepository.save(user);

            // Async credit score calculation
            creditScoreService.calculateCreditScore(user,
                transactionService.loadTransactionsFromCSV());

            log.info("Registered User Successfully with uniqueUserId: {}", user.getUniqueUserId());
            return ResponseEntity.ok(UserRegistrationResponse.builder()
                .uniqueUserId(user.getUniqueUserId())
                .status("Registered Successfully")
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserRegistrationResponse.builder()
                .status("Registration Failed")
                .error(e.getMessage())
                .build());
        }
    }

    @PostMapping("/apply-loan")
    public ResponseEntity<LoanApplicationResponse> applyLoan(
        @RequestBody LoanApplicationRequest request) {
        try {
            Loan loan = loanService.createLoan(
                request.getUniqueUserId(),
                request.getLoanType(),
                request.getLoanAmount(),
                request.getInterestRate(),
                request.getTermPeriod(),
                request.getDisbursementDate()
            );

            List<EMISchedule> emiSchedules = loan.getEmis().stream()
                .map(emi -> EMISchedule.builder()
                    .date(emi.getDueDate())
                    .amountDue(emi.getAmountDue())
                    .build())
                .collect(Collectors.toList());

            log.info("Loan application processed successfully for loanId : {}", loan.getLoanId());
            return ResponseEntity.ok(LoanApplicationResponse.builder()
                .status("Loan application processed successfully")
                .loanId(loan.getLoanId())
                .dueDates(emiSchedules)
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoanApplicationResponse.builder()
                .status("Loan application failed")
                .error(e.getMessage())
                .build());
        }
    }

    @PostMapping("/make-payment")
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = new PaymentResponse();
        try {
            Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

            EMI unpaidEMI = loan.getEmis().stream()
                .filter(emi -> !emi.getIsPaid())
                .min(Comparator.comparing(EMI::getDueDate))
                .orElseThrow(() -> new RuntimeException("No pending EMIs"));

            if (!request.getAmount().equals(unpaidEMI.getAmountDue())) {
                throw new RuntimeException("Payment amount must match EMI amount");
            }

            unpaidEMI.setIsPaid(true);
            loanRepository.save(loan);
            log.info("Payment successfully processed for loanId : {}", loan.getLoanId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(PaymentResponse.builder()
                .status("Payment processing failed")
                .error(e.getMessage())
                .build());
        }
    }

    @GetMapping("/get-statement")
    public ResponseEntity<LoanStatementResponse> getStatement(@RequestParam String loanId) {
        LoanStatementResponse response = new LoanStatementResponse();
        try {
            Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

            response.setPastTransactions(loan.getEmis().stream()
                .filter(EMI::getIsPaid)
                .map(emi -> PastTransaction.builder()
                    .amountPaid(emi.getAmountDue())
                    .date(emi.getDueDate())
                    .interest(emi.getInterestComponent())
                    .principal(emi.getPrincipalComponent())
                    .build())
                .collect(Collectors.toList()));

            response.setUpcomingTransactions(loan.getEmis().stream()
                .filter(emi -> !emi.getIsPaid())
                .map(emi -> UpcomingTransaction.builder()
                    .date(emi.getDueDate())
                    .amountDue(emi.getAmountDue())
                    .build())
                .collect(Collectors.toList()));

            log.info("Statement fetched successfully for loanId : {}", loan.getLoanId());

            response.setStatus("Statement fetched successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError(e.getMessage());
            return ResponseEntity.badRequest().body(LoanStatementResponse.builder()
                .error(e.getMessage())
                .status("error occurred in fetching statement")
                .build());
        }
    }
} 