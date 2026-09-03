// src/main/java/com/fintech/api/repository/TransactionRepository.java
package com.fintech.api.repository;

import com.fintech.api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Тухайн Account дээр хийгдсэн бүх гүйлгээг олох
    // (Account.id-гаар шүүнэ, findByAccountId гэдэг нэрийг Spring өөрөө SQL болгож хувиргана)
    List<Transaction> findByAccountId(Long accountId);
}