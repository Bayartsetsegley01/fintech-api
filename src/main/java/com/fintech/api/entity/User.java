// src/main/java/com/fintech/api/entity/User.java
package com.fintech.api.entity;

// JPA-ийн annotation-ууд: @Entity, @Id, @Column, @GeneratedValue гэх мэт
import jakarta.persistence.*;
// Lombok: getter method-уудыг автоматаар үүсгэнэ (getName(), getEmail() гэх мэт)
import lombok.Getter;
// Lombok: параметргүй (хоосон) constructor-ыг автоматаар үүсгэнэ, JPA-д заавал хэрэгтэй
import lombok.NoArgsConstructor;
// Lombok: setter method-уудыг автоматаар үүсгэнэ (setName(), setEmail() гэх мэт)
import lombok.Setter;

// Огноо, цаг хадгалах Java стандарт класс
import java.time.LocalDateTime;

// Энэ класс database table-тэй холбоотой гэдгийг Hibernate-д мэдэгдэнэ
@Entity
// Table-ийн нэрийг "users" гэж тодорхой заана (заагаагүй бол класс нэрээр автомат үүснэ)
@Table(name = "users")
// Lombok: доорх бүх талбарт getter method автоматаар нэмнэ
@Getter
// Lombok: доорх бүх талбарт setter method автоматаар нэмнэ
@Setter
// Lombok: параметргүй constructor нэмнэ — JPA объект үүсгэхдээ үүнийг ашигладаг
@NoArgsConstructor
public class User {

    // Энэ талбар нь table-ийн primary key (өвөрмөц ID) гэдгийг заана
    @Id
    // ID-г database өөрөө автоматаар нэмэгдүүлж үүсгэнэ (1, 2, 3, 4...)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "name" багана хоосон (NULL) байж болохгүй гэдгийг зааж, DB constraint үүсгэнэ
    @Column(nullable = false)
    private String name;

    // "email" багана хоосон байж болохгүй, мөн давхардсан утга орж болохгүй (unique)
    @Column(nullable = false, unique = true)
    private String email;

    // Багана нэрийг "created_at" гэж тодорхой заана.
    // updatable = false гэдэг нь энэ утгыг цаашид өөрчлөх боломжгүй гэсэн үг (зөвхөн анх удаа бичигдэнэ)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Энэ метод нь объектыг database руу ХАДГАЛАХААС ӨМНӨ автоматаар дуудагдана
    @PrePersist
    protected void onCreate() {
        // createdAt талбарыг яг одоогийн огноо, цагаар автоматаар дүүргэнэ
        // (гараар өгөх шаардлагагүй болно)
        this.createdAt = LocalDateTime.now();
    }
}