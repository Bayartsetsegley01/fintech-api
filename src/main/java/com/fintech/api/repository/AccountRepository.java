// src/main/java/com/fintech/api/repository/AccountRepository.java
package com.fintech.api.repository;

// Account entity классыг импортлоно
import com.fintech.api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// JpaRepository<Account, Long> — Account entity-тэй ажиллана, ID-ийн төрөл нь Long
// save(), findById(), findAll(), deleteById() гэх мэт CRUD метод бүгд автоматаар бэлэн болно
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Дансны дугаараар хайх — метод нэрээр Spring өөрөө SQL query автоматаар үүсгэдэг
    Optional<Account> findByAccountNumber(String accountNumber);

    // Тухайн хэрэглэгчийн (User) бүх дансыг олох
    // "findByUserId" гэдэг нэрийг уншаад, Account.user.id-гаар шүүх query автоматаар үүснэ
    List<Account> findByUserId(Long userId);
}