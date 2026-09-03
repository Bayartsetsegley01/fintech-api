// src/main/java/com/fintech/api/controller/TransactionController.java
package com.fintech.api.controller;

import com.fintech.api.entity.Transaction;
import com.fintech.api.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // POST /api/transactions/deposit — данс руу мөнгө хийх
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody DepositWithdrawRequest request) {
        Transaction transaction = transactionService.deposit(
                request.accountId(),
                request.amount(),
                request.description()
        );
        return ResponseEntity.status(201).body(transaction);
    }

    // POST /api/transactions/withdraw — данснаас мөнгө татах
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody DepositWithdrawRequest request) {
        Transaction transaction = transactionService.withdraw(
                request.accountId(),
                request.amount(),
                request.description()
        );
        return ResponseEntity.status(201).body(transaction);
    }

    // POST /api/transactions/transfer — данснаас данс руу шилжүүлэх
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        transactionService.transfer(
                request.fromAccountId(),
                request.toAccountId(),
                request.amount(),
                request.description()
        );
        return ResponseEntity.status(201).build();
    }

    // GET /api/transactions/{accountId} — тухайн дансны гүйлгээний түүх
    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    // DEPOSIT болон WITHDRAW хүсэлтийн JSON body-г хүлээж авах жижиг класс
    public record DepositWithdrawRequest(Long accountId, BigDecimal amount, String description) {}

    // TRANSFER хүсэлтийн JSON body-г хүлээж авах жижиг класс
    public record TransferRequest(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {}
}