// src/main/java/com/fintech/api/exception/GlobalExceptionHandler.java
package com.fintech.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

// @RestControllerAdvice нь бүх Controller-т нэгдсэн байдлаар алдаа барих боломж олгоно —
// Controller бүрт try/catch бичих шаардлагагүй болгодог
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid validation амжилтгүй болоход (жишээ нь @NotBlank, @DecimalMin) Spring
    // энэ Exception-ыг автоматаар шидэж, доорх метод барьж авна
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        // Алдаатай талбар бүрийг цуглуулж, талбарын нэр -> алдааны мессеж гэсэн map үүсгэнэ
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, "Validation failed");
        body.put("fields", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    // AccountService, UserService дотор бид throw хийдэг "not found" төрлийн алдааг барина
    @ExceptionHandler({AccountNotFoundException.class, RuntimeException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        Map<String, Object> body = buildBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // Insufficient balance, self-transfer, negative amount зэрэг бизнес логикийн алдааг барина
    @ExceptionHandler({InsufficientBalanceException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    // Бүх хариу нэг стандарт бүтэцтэй байхын тулд ашигладаг туслах метод
    private Map<String, Object> buildBody(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
}