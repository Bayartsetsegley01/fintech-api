// src/main/java/com/fintech/api/controller/UserController.java
package com.fintech.api.controller;

// User entity классыг импортлоно
import com.fintech.api.entity.User;
// Бизнес логик агуулсан service давхаргыг импортлоно
import com.fintech.api.service.UserService;
// HTTP статус кодуудыг (200, 201, 404 гэх мэт) удирдахад хэрэглэгддэг класс
import org.springframework.http.ResponseEntity;
// REST API-ийн annotation-ууд (@GetMapping, @PostMapping гэх мэт)
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Энэ класс нь REST API controller гэдгийг зааж, буцаах утгыг автоматаар JSON болгож хувиргана
@RestController
// Энэ controller доторх бүх endpoint "/api/users"-ээр эхэлнэ
@RequestMapping("/api/users")
public class UserController {

    // Бизнес логикийг гүйцэтгэдэг service-ийг Spring автоматаар дамжуулна
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/users — шинэ хэрэглэгч үүсгэх endpoint
    @PostMapping
    // @RequestBody нь HTTP request-ийн JSON body-г автоматаар User объект болгож хувиргана
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        // 201 CREATED статус кодтойгоор шинээр үүссэн хэрэглэгчийг JSON болгож буцаана
        return ResponseEntity.status(201).body(createdUser);
    }

    // GET /api/users/{id} — ID-гаар нэг хэрэглэгчийг авах endpoint
    @GetMapping("/{id}")
    // @PathVariable нь URL доторх {id} хэсгийг автоматаар метод-ийн параметр рүү дамжуулна
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        // 200 OK статустайгаар хэрэглэгчийг буцаана
        return ResponseEntity.ok(user);
    }

    // GET /api/users — бүх хэрэглэгчийн жагсаалтыг авах endpoint
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}