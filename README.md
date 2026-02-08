# Collab Notes Backend

Collaborative notes REST API built with Spring Boot.  
The project demonstrates production-style backend architecture including JWT authentication, PostgreSQL persistence, and Dockerized local development.

---

## 🎯 Project Purpose

This project was built to demonstrate real-world backend development patterns including:

- Secure JWT authentication
- Layered Spring Boot architecture
- PostgreSQL database integration
- Docker-based local development setup
- Environment-based configuration management

## 🚀 Tech Stack

- Java 21
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- PostgreSQL
- Docker (local PostgreSQL container)
- Maven

---

## 📂 Architecture

controller → service → repository → database
+ DTO / Mapper layer

Layers included:
- Controllers (REST API)
- Services (business logic)
- Repositories (data access)
- DTO + Mapper layer
- Security (JWT + Filter + Spring Security config)

---

## 🔐 Authentication

- JWT Access Token authentication
- Password hashing using BCrypt
- Stateless security configuration

---

## 🐳 Running Locally

### 1️⃣ Clone repo
```bash
git clone https://github.com/gandii99/collab-notes-backend.git
cd collab-notes-backend
```


---

### 2️⃣ Start PostgreSQL (Docker)
```bash
docker compose up -d
```

---

### 3️⃣ Create environment variables

Values can be provided using system environment variables or `.env` file
```env
DB_URL=jdbc:postgresql://localhost:5432/collabnotes
DB_USERNAME=postgres
DB_PASSWORD=postgres

JWT_SECRET=your-super-secure-secret-min-32-characters
JWT_EXPIRATION=86400000
```

---

### 4️⃣ Run application

Using Maven wrapper:
```bash
./mvnw spring-boot:run
```
Or Windows:
```bash
mvnw.cmd spring-boot:run
```

---

## 🌐 API Base URL
http://localhost:8080


---

## 📌 Example Endpoints

### Auth
```http
POST /auth/login
GET /auth/me
```
---

### Users
```http
GET /users
POST /users
GET /users/{id}
```

---

### Notes
```http
GET /notes
GET /notes?userId={id}
GET /notes/{id}
POST /notes
```

---

## 🔑 JWT Usage

Add header:
```http
Authorization: Bearer <token>
```
---

## 🧪 Testing

You can test API using:
- Postman
- Insomnia
- curl

---

## 📈 Future Improvements

- Refresh tokens
- Role-based authorization
- Pagination
- Global exception handling
- Integration tests
- CI/CD pipeline

---

## 🧑‍💻 Author
GitHub: https://github.com/gandii99

---

## 📜 License

For educational and portfolio purposes.
