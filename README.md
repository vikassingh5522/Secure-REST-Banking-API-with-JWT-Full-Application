

# Secure REST Banking API with JWT – Full Application

Secure REST Banking API with JWT – Full Application

A full-stack banking application built with Spring Boot (Java) for the backend and React (Vite + Tailwind) for the frontend.
Implements JWT-based authentication for secure user management and supports core banking features: check balance, deposit, withdraw, and transfer funds.

Backend: Spring Boot 3.x, Spring Security, JJWT, Spring Data JPA, MySQL
Frontend: React 18.x (Vite), Axios, Tailwind CSS
Database: MySQL 8.xA complete full-stack banking application with secure authentication, role-based authorization, and real-time account operations.
The backend is built with **Spring Boot**, **Spring Security**, **JWT**, and **MySQL**, while the frontend is built with **React**, **Vite**, and **Tailwind CSS**.
Features
Authentication & Security

User registration & login with JWT (JSON Web Token)

Stateless authentication with custom JwtFilter

Role-based access control with .hasRole() and .hasAuthority()

Passwords hashed with BCrypt

Banking Operations

Check Balance

Deposit Funds ✅ (Fixed: updates & displays correctly)

Withdraw Funds

Transfer Funds between accounts

Transaction Records stored in DB

Frontend

Register and Login forms

Dashboard with account balance and transaction options

JWT stored in localStorage for persistent sessions
---

## 📸 Screenshots

*(Replace these with actual image paths in your repo or keep GitHub-hosted links)*

<img width="1847" height="1018" alt="image" src="https://github.com/user-attachments/assets/88b5fa55-e880-4b81-a9be-5213216a29aa" />
<img width="869" height="935" alt="image" src="https://github.com/user-attachments/assets/9c8b4cea-137a-44ae-927d-36a06d265f07" />
<img width="635" height="770" alt="image" src="https://github.com/user-attachments/assets/eaddc0e0-3e22-4b35-b52a-82289d2f083c" />
<img width="675" height="833" alt="image" src="https://github.com/user-attachments/assets/0e385c0f-0649-411f-a695-6ee095896a2e" />
<img width="810" height="881" alt="image" src="https://github.com/user-attachments/assets/397ec69b-c7e1-403f-bd9d-b2bd7044884e" />

---

## 🚀 Features

* **🔑 Authentication** – User registration & login with JWT-based stateless authentication.
* **🛡 Authorization** – Role-based access control with `ROLE_USER`.
* **🔒 Secure API** – Custom `JwtFilter` to validate tokens for protected endpoints.
* **🏦 Banking Operations**:

  * Check account balance
  * Deposit funds
  * Withdraw funds
  * Transfer funds between users
* **💾 Database** – MySQL for storing users, accounts, and transaction history.
* **🎨 Frontend** – React + Tailwind CSS UI for banking actions.

---

## 📂 Project Structure

```
secure-rest-banking-api-jwt/
│
├── backend/  (Spring Boot Application)
│   ├── config/       # Security & password encoding configuration
│   ├── security/     # JWT utilities and filters
│   ├── entity/       # User, Account, Transaction entities
│   ├── repository/   # Spring Data JPA repositories
│   ├── service/      # Business logic for users/accounts
│   ├── controller/   # API endpoints
│   └── resources/    # application.properties
│
└── frontend/ (React + Vite + Tailwind CSS)
    ├── components/   # Login, Register, Dashboard
    ├── App.jsx       # Routes & layout
    └── api/          # Axios API calls
```

---

## ⚙️ Backend – Spring Boot

**Main Features:**

* **Spring Security** – Configured with JWT and stateless authentication.
* **BCryptPasswordEncoder** – For secure password hashing.
* **Role-based Access** – `.hasRole("USER")` for banking operations.
* **JPA + MySQL** – Auto-generates tables for User, Account, and Transaction.

**Key Classes:**

* `SecurityConfig.java` – Security rules & JWT filter.
* `JwtUtil.java` – Generate, validate, and parse JWTs.
* `JwtFilter.java` – Intercepts requests & checks tokens.
* `UserService.java` – Handles registration & automatic account creation.
* `AccountService.java` – Handles deposits, withdrawals, and transfers.

---

## 🎨 Frontend – React + Vite + Tailwind CSS

**Main Features:**

* **JWT-based login** stored in `localStorage`
* **Protected routes** (Dashboard)
* **Axios** for API calls
* **Tailwind CSS** for styling

**Pages:**

* `Login.jsx` – Authenticates & stores JWT
* `Register.jsx` – Creates account & redirects to login
* `Dashboard.jsx` – Shows balance, deposit, withdraw, and transfer options

---

## 🗄 Database – MySQL

**Tables (auto-created by JPA):**

* `users` – Username, hashed password, role
* `account` – Linked to user, balance
* `transaction` – Records all deposit/withdraw/transfer actions

---

## 🛠 Setup Instructions

### **1. Backend Setup**

```bash
git clone <repo-url>
cd backend
```

Create MySQL database:

```sql
CREATE DATABASE banking_db;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

jwt.secret=CHANGE_THIS_SECRET
jwt.expiration=3600000
```

Run backend:

```bash
mvn clean install
mvn spring-boot:run
```

Backend runs at: **[http://localhost:8080](http://localhost:8080)**

---

### **2. Frontend Setup**

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at: **[http://localhost:5173](http://localhost:5173)** (Vite default)

---

## 🔒 Security Details

* **JWT Authentication**

  * Generated at login
  * Includes username & role
  * 1-hour expiration
* **Password Security**

  * Stored with BCrypt hashing
* **Role-based Access**

  * Only `ROLE_USER` can use banking APIs
* **Stateless**

  * No sessions stored server-side

---

## 📌 Usage Flow

1. **Register** → `/register`
2. **Login** → `/login` (stores JWT)
3. **Dashboard** → `/dashboard`

   * View balance
   * Deposit / Withdraw
   * Transfer to another user
4. **Logout** → Clears JWT

---

## 🧪 API Endpoints

**Public:**

* `POST /api/auth/register` → Register new user
* `POST /api/auth/login` → Authenticate & get JWT

**Protected (JWT required):**

* `GET /api/account/balance`
* `POST /api/account/deposit`
* `POST /api/account/withdraw`
* `POST /api/account/transfer`

---

## 🔮 Future Improvements

* Transaction history in UI
* Admin role with user management
* Refresh tokens
* Multi-factor authentication

---


