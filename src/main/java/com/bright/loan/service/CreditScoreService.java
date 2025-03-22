package com.bright.loan.service;

import com.bright.loan.constant.AppConstants;
import com.bright.loan.model.Transaction;
import com.bright.loan.model.User;
import com.bright.loan.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class CreditScoreService {

    @Autowired
    private UserRepository userRepository;

    @Async
    public void calculateCreditScore(User user, List<Transaction> transactions) {
        double balance = transactions.stream()
            .filter(transaction -> transaction.getAadharId().equals(user.getAadharId()))
            .mapToDouble(
                transaction -> AppConstants.CREDIT.equals(transaction.getTransactionType()) ?
                    transaction.getAmount() : -transaction.getAmount())
            .sum();

        int creditScore;
        if (balance >= AppConstants.MAX_CREDIT_BALANCE) {
            creditScore = AppConstants.MAX_CREDIT_SCORE;
        } else if (balance <= AppConstants.MIN_CREDIT_BALANCE) {
            creditScore = AppConstants.MIN_CREDIT_SCORE;
        } else {
            creditScore = AppConstants.MIN_CREDIT_SCORE
                + (int) ((balance - AppConstants.MIN_CREDIT_BALANCE) / 15000) * 10;
            creditScore = Math.min(AppConstants.MAX_CREDIT_SCORE, creditScore);
        }

        user.setCreditScore(creditScore);
        log.info("Calculated Credit Score and Added the user successfully with details {}",user);
        userRepository.save(user);
    }
} 