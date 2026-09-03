// src/main/java/com/fintech/api/entity/Transaction.java
package com.fintech.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Энэ гүйлгээ аль Account дээр бүртгэгдэж байгааг заана
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Гүйлгээний төрөл: DEPOSIT, WITHDRAW, эсвэл TRANSFER
    // @Enumerated(STRING) ашигласнаар database дээр "0,1,2" гэсэн тоо биш,
    // "DEPOSIT" гэх мэт унших боломжтой текст хэлбэрээр хадгална
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Гүйлгээний 3 боломжит төрлийг тодорхойлсон enum
    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER
    }
}