# FinTech Transaction API

Хэрэглэгч, данс, гүйлгээ удирдах backend REST API. Java 21, Spring Boot, PostgreSQL дээр бүтээсэн — React frontend-тэй.

## Tech Stack

**Backend:** Java 21, Spring Boot, Spring Data JPA, PostgreSQL, Maven, Lombok
**Frontend:** React, Vite, Tailwind CSS

## Боломжууд

- Хэрэглэгч, данс, гүйлгээ (deposit / withdraw / transfer) удирдах
- `BigDecimal` ашигласан мөнгөн тооцоо
- Balance validation (хүрэлцэхгүй үлдэгдэл, сөрөг дүн, өөрийн рүүгээ шилжүүлэх хориглосон)
- `@Transactional` — transfer амжилтгүй бол бүх өөрчлөлт буцаагдана
- DTO + Bean Validation (`@NotBlank`, `@DecimalMin` гэх мэт)
- Global Exception Handler — талбар тус бүрийн алдааг тодорхой JSON-оор буцаана
- Гүйлгээний бүрэн түүх

## API Endpoints

| Method | Endpoint | Тайлбар |
|---|---|---|
| POST | `/api/users` | Хэрэглэгч үүсгэх |
| GET | `/api/users/{id}` | Хэрэглэгч авах |
| GET | `/api/users` | Бүх хэрэглэгч |
| POST | `/api/accounts` | Данс үүсгэх |
| GET | `/api/accounts/{id}` | Данс авах |
| GET | `/api/accounts/{id}/balance` | Үлдэгдэл авах |
| GET | `/api/accounts/user/{userId}` | Хэрэглэгчийн бүх данс |
| POST | `/api/transactions/deposit` | Мөнгө хийх |
| POST | `/api/transactions/withdraw` | Мөнгө татах |
| POST | `/api/transactions/transfer` | Данс хооронд шилжүүлэх |
| GET | `/api/transactions/{accountId}` | Гүйлгээний түүх |

## Ажиллуулах

```bash
# Database бэлдэх
psql postgres
CREATE DATABASE fintech_api;
CREATE USER fintech_user WITH PASSWORD 'fintech123';
GRANT ALL PRIVILEGES ON DATABASE fintech_api TO fintech_user;

# Backend
./mvnw spring-boot:run   # http://localhost:8080

# Frontend
cd frontend && npm install && npm run dev   # http://localhost:5173
```

## Дараагийн сайжруулалт

- Unit тест (JUnit)
- Swagger / OpenAPI баримтжуулалт
- JWT authentication
- Docker
