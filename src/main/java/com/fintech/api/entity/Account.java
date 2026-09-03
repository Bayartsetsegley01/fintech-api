// src/main/java/com/fintech/api/entity/Account.java
package com.fintech.api.entity;

// JPA-ийн annotation-ууд
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Энэ класс database table-тэй холбоотой гэдгийг Hibernate-д мэдэгдэнэ
@Entity
// Table-ийн нэрийг "accounts" гэж тодорхой заана
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    // Primary key, автоматаар нэмэгддэг ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Олон Account нэг User-т харьяалагдана (Many-to-One холбоос)
    @ManyToOne
    // Энэ Account аль User-т харьяалагдахыг заадаг "user_id" гэсэн foreign key багана үүснэ
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Дансны дугаар, давхардахгүй байх ёстой
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    // Мөнгөн дүн — BigDecimal ашиглана (double ашиглахгүй, учир нь мөнгөн тооцоонд
    // floating-point алдаа гарч болзошгүй тул санхүүгийн систем БҮГД BigDecimal ашигладаг)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    // Данс үүссэн огноо, цаг
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Database руу хадгалахаас өмнө автоматаар createdAt-ыг дүүргэнэ
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}