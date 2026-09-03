# FinTech Transaction API

A backend REST API for managing users, financial accounts, and transactions — built to demonstrate core FinTech backend concepts like balance validation and atomic transactions.

## Tech Stack

- Java 21
- Spring Boot 4.1
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Lombok

## Features

- User management (create, retrieve)
- Account management, linked to users
- Deposit, withdrawal, and transfer transactions
- Balance validation (insufficient funds, negative amounts, self-transfer prevention)
- Full transaction history per account
- Atomic transactions using `@Transactional` — ensures money is never lost or duplicated during transfers

