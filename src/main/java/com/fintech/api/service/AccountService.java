// src/main/java/com/fintech/api/service/AccountService.java
package com.fintech.api.service;

import com.fintech.api.entity.Account;
import com.fintech.api.entity.User;
import com.fintech.api.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    // Данс болон хэрэглэгчийн repository хоёуланг Spring автоматаар дамжуулна
    private final AccountRepository accountRepository;
    private final UserService userService;

    // Constructor injection — Spring өөрөө хоёр repository/service-ийг автоматаар дамжуулна
    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    // Шинэ данс үүсгэх метод
    public Account createAccount(Long userId, String accountNumber, BigDecimal initialBalance) {

        // Эхний үлдэгдэл сөрөг байж болохгүй гэдгийг шалгана
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        // Тухайн userId-тай хэрэглэгч бодитоор байгаа эсэхийг шалгана
        // (UserService-ийн getUserById метод хэрэглэгч олдохгүй бол өөрөө алдаа шиднэ)
        User user = userService.getUserById(userId);

        // Дансны дугаар аль хэдийн ашиглагдсан эсэхийг шалгана
        accountRepository.findByAccountNumber(accountNumber)
                .ifPresent(a -> {
                    throw new IllegalArgumentException("Account number already exists: " + accountNumber);
                });

        // Шинэ Account объект үүсгэж, талбаруудыг дүүргэнэ
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(accountNumber);
        account.setBalance(initialBalance);

        // Database руу хадгална
        return accountRepository.save(account);
    }

    // ID-гаар данс хайх
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    // Зөвхөн үлдэгдлийг авах (getAccountById ашиглаад balance-ийг нь буцаана)
    public BigDecimal getBalance(Long id) {
        Account account = getAccountById(id);
        return account.getBalance();
    }

    // Тухайн хэрэглэгчийн бүх дансыг авах
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }
}