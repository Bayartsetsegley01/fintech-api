// src/main/java/com/fintech/api/controller/TransactionController.java
package com.fintech.api.controller;

import com.fintech.api.dto.DepositWithdrawRequest;
import com.fintech.api.dto.TransferRequest;
import com.fintech.api.entity.Transaction;
import com.fintech.api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody DepositWithdrawRequest request) {
        Transaction transaction = transactionService.deposit(
                request.accountId(), request.amount(), request.description()
        );
        return ResponseEntity.status(201).body(transaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@Valid @RequestBody DepositWithdrawRequest request) {
        Transaction transaction = transactionService.withdraw(
                request.accountId(), request.amount(), request.description()
        );
        return ResponseEntity.status(201).body(transaction);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        transactionService.transfer(
                request.fromAccountId(), request.toAccountId(), request.amount(), request.description()
        );
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }
}