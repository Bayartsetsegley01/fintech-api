// src/main/java/com/fintech/api/service/UserService.java
package com.fintech.api.service;

// User entity классыг импортлоно
import com.fintech.api.entity.User;
// Database давхаргатай харилцах repository-г импортлоно
import com.fintech.api.repository.UserRepository;
// Spring-д "энэ класс бол бизнес логикийн давхарга" гэдгийг зааж, автоматаар bean болгож бүртгүүлнэ
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    // Repository-г шууд "new" хийж үүсгэхгүй, Spring өөрөө автоматаар оруулж өгнө (Dependency Injection)
    private final UserRepository userRepository;

    // Constructor — Spring энэ constructor-ыг дуудаж, UserRepository bean-ийг автоматаар дамжуулна
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Шинэ хэрэглэгч үүсгэх метод
    public User createUser(User user) {
        // Repository-ийн save() методыг дуудна — энэ нь INSERT SQL query автоматаар үүсгэж ажиллуулна
        return userRepository.save(user);
    }

    // ID-гаар хэрэглэгч хайх метод
    public User getUserById(Long id) {
        // findById нь Optional<User> буцаадаг тул орлогчгүй бол алдаа шидэх код бичих ёстой
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Бүх хэрэглэгчийн жагсаалт авах метод
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}