package com.bright.loan.service;

import com.bright.loan.constant.AppConstants;
import com.bright.loan.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TransactionService {
    
    public List<Transaction> loadTransactionsFromCSV() {
        List<Transaction> transactions = new ArrayList<>();
        Path path = Paths.get(AppConstants.CSV_DATA_FILE);
        
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 4) {
                    Transaction transaction = Transaction.builder()
                        .aadharId(values[0].trim())
                        .date(LocalDate.parse(values[1].trim()))
                        .transactionType(values[2].trim())
                        .amount(Double.parseDouble(values[3].trim()))
                        .build();
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            log.error("Failed to read transaction data with error {}",e.getMessage());
            throw new RuntimeException();
        }
        
        return transactions;
    }
} 