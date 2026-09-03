// src/main/java/com/fintech/api/repository/UserRepository.java
package com.fintech.api.repository;

// User entity классыг импортлоно, учир нь энэ repository яг User объектуудтай ажиллана
import com.fintech.api.entity.User;
// Spring Data JPA-ийн бэлэн интерфейс — CRUD (Create, Read, Update, Delete) метод бүгдийг агуулдаг
import org.springframework.data.jpa.repository.JpaRepository;
// Spring-д "энэ интерфэйс нь database давхарга" гэдгийг зааж, автоматаар bean болгож бүртгүүлнэ
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// JpaRepository<User, Long> гэдэг нь:
//   - User = аль Entity-тэй ажиллаж байгаа
//   - Long = тэр Entity-ийн ID-ийн төрөл (User.id нь Long тул)
// Үүнийг extend хийхэд save(), findById(), findAll(), deleteById() гэх мэт
// метод бүгд АВТОМАТААР бэлэн болно — өөрөө нэг мөр ч SQL бичих шаардлагагүй
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA-ийн "query method" — метод нэрээрээ SQL query автоматаар үүсгэдэг онцлог
    // "findByEmail" гэдэг нэрийг уншаад, "email баганаар хайх SELECT query" гэдгийг өөрөө таана
    // Optional<User> ашигласнаар "олдохгүй байж ч болно" гэдгийг илэрхийлж, NullPointerException-ээс сэргийлнэ
    Optional<User> findByEmail(String email);
}