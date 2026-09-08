# 🎮 Gaming Lounge Management System

A full-stack management system for a gaming lounge — handles station bookings, memberships, food ordering, payments, invoicing, and admin analytics. Built as a final coursework project (IJSE) with a Spring Boot REST backend and a vanilla JS / jQuery frontend.

## Overview

Gaming Lounge lets customers book gaming stations, order snacks, and manage memberships, while admins/employees manage branches, stations, stock, and view revenue analytics — all behind JWT-secured role-based access (Admin / Employee / Customer / Guest).

## Features

- **Auth & Security** — JWT-based login, OTP email verification on signup, role-based access control (Spring Security), guest token access for limited browsing
- **Bookings** — Station booking with auto-completion of stale bookings via a scheduled job
- **Memberships** — Membership plans with discount tiers applied automatically to orders
- **Food Ordering** — Snack catalog with stock tracking, automatic low-stock email alerts to admin on create/update/order
- **Payments & Invoicing** — Payment recording and invoice generation
- **Feedback** — Customer feedback collection
- **Admin Analytics** — Revenue dashboard (Chart.js) for admins
- **Chatbot** — In-app assistant widget for customer queries
- **Soft Delete** — Restore-capable soft deletion across core entities (snacks, stations, etc.)

## Tech Stack

**Backend:** Spring Boot 4.1, Spring Data JPA, Spring Security, Spring Mail, MySQL
**Frontend:** HTML/CSS, vanilla JavaScript, jQuery, Chart.js
**Build:** Maven

## Project Structure

```
src/main/java/edu/ijse/gamingLounge/
├── controller/      REST endpoints
├── service/         Business logic (interface + impl per feature)
├── repository/      Spring Data JPA repositories
├── entity/          JPA entities
├── dto/             Request/response DTOs
├── security/         JWT filter, security config, user details
├── util/            EmailService and other shared helpers
└── exception/       BusinessException + global exception handling
src/main/resources/
└── static/          Frontend (HTML/CSS/JS)
```

## Getting Started

### Prerequisites
- Java 25
- Maven 3.9+
- MySQL 8+
- A Gmail account with an [app password](https://support.google.com/accounts/answer/185833) (for OTP/low-stock emails)

### Setup

1. Clone the repo
   ```bash
   git clone https://github.com/<your-username>/gamingLounge.git
   cd gamingLounge
   ```

2. Create the MySQL database

3. Set the required environment variables:

   | Variable | Purpose |
   |---|---|
   | `JWT_SECRET` | Secret key used to sign JWTs |
   | `MAIL_USERNAME` | Gmail address used to send OTP/alert emails |
   | `MAIL_PASSWORD` | Gmail app password |

4. Run the app
   ```bash
   ./mvnw spring-boot:run
   ```

5. App runs at `http://localhost:8080`

## Configuration

Key settings in `application.properties`:

| Property | Default | Description |
|---|---|---|
| `gaminglounge.admin.email` | — | Where low-stock alerts are sent |
| `gaminglounge.lowstock.threshold` | `5` | Stock qty at/below which an alert fires |
| `jwt.expiration` | `86400000` | Token lifetime (ms) — 24h |

## Author

Thushal — IJSE AAD Final Coursework
