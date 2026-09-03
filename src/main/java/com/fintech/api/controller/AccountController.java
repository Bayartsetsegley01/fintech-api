// src/main/java/com/fintech/api/controller/AccountController.java
package com.fintech.api.controller;

import com.fintech.api.entity.Account;
import com.fintech.api.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // POST /api/accounts — шинэ данс үүсгэх endpoint
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(
                request.userId(),
                request.accountNumber(),
                request.initialBalance()
        );
        return ResponseEntity.status(201).body(account);
    }

    // GET /api/accounts/{id} — ID-гаар нэг дансны мэдээлэл авах
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    // GET /api/accounts/{id}/balance — зөвхөн үлдэгдлийг авах
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        BigDecimal balance = accountService.getBalance(id);
        return ResponseEntity.ok(balance);
    }

    // GET /api/accounts/user/{userId} — тухайн хэрэглэгчийн бүх дансыг авах
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    // POST хүсэлтийн JSON body-г хүлээж авахад ашиглах жижиг record класс
    // (userId, accountNumber, initialBalance гэсэн 3 талбарыг агуулна)
    public record CreateAccountRequest(Long userId, String accountNumber, BigDecimal initialBalance) {}
}