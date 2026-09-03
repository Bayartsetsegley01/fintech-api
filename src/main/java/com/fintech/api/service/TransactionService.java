// src/main/java/com/fintech/api/service/TransactionService.java
package com.fintech.api.service;

import com.fintech.api.entity.Account;
import com.fintech.api.entity.Transaction;
import com.fintech.api.entity.Transaction.TransactionType;
import com.fintech.api.repository.AccountRepository;
import com.fintech.api.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    // DEPOSIT — данс руу мөнгө хийх
    // @Transactional гэдэг нь: энэ метод дотор database дээр хийгдэх бүх өөрчлөлт
    // НЭГ бүхэл ажиллагаа (atomic) гэдгийг зааж, дунд нь ямар нэг алдаа гарвал
    // бүх өөрчлөлтийг АВТОМАТААР буцаана (rollback)
    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount, String description) {

        // Сөрөг эсвэл 0 дүнтэй гүйлгээг хориглоно
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        // Дансны үлдэгдэлд мөнгө нэмнэ
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Гүйлгээний бүртгэл (transaction log) үүсгэж хадгална
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }

    // WITHDRAW — данснаас мөнгө татах
    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount, String description) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        // Үлдэгдэл хүрэлцэхгүй бол алдаа шидэнэ (энэ бол FinTech-ийн хамгийн чухал шалгалт)
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Дансны үлдэгдлээс мөнгө хасна
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }

    // TRANSFER — нэг дансаас нөгөө данс руу мөнгө шилжүүлэх
    // Энэ бол хамгийн чухал метод: 2 дансны өөрчлөлт хоёулаа АМЖИЛТТАЙ
    // эсвэл хоёулаа БУЦААГДСАН байх ёстой (@Transactional нь үүнийг баталгаажуулна)
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // Өөрийн рүүгээ шилжүүлэхийг хориглоно
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found with id: " + fromAccountId));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found with id: " + toAccountId));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Илгээгчээс хасна
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        // Хүлээн авагчид нэмнэ
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Илгээгчийн талд WITHDRAW гүйлгээ бүртгэнэ
        Transaction outgoing = new Transaction();
        outgoing.setAccount(fromAccount);
        outgoing.setType(TransactionType.TRANSFER);
        outgoing.setAmount(amount);
        outgoing.setDescription("Transfer to account " + toAccountId + ": " + description);
        transactionRepository.save(outgoing);

        // Хүлээн авагчийн талд DEPOSIT гүйлгээ бүртгэнэ
        Transaction incoming = new Transaction();
        incoming.setAccount(toAccount);
        incoming.setType(TransactionType.TRANSFER);
        incoming.setAmount(amount);
        incoming.setDescription("Transfer from account " + fromAccountId + ": " + description);
        transactionRepository.save(incoming);
    }

    // Тухайн дансны бүх гүйлгээний түүхийг авах
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}