# 💠 GYM TASK APPLICATION – REST EDITION 💠


The **Gym Task** project is a robust Spring Boot + Hibernate REST API application. <br>
It manages Trainers, Trainees, bidirectional Trainer ↔ Trainee assignments, Trainings, authentication, profile lifecycle, error handling, documentation with Swagger, and more. <br>
Ensuring security, transaction management, and comprehensive validation.

---

## ✨ Key Features ✨

| Area                              | Endpoints / Use-Cases                                                                                       |
| --------------------------------- | ----------------------------------------------------------------------------------------------------------- |
| **Account Creation**              | Trainer & Trainee registration with **auto-generated, BCrypt-hashed credentials**                           |
| **Authentication**                | Username + Password login → JWT issued · Change Password · **Brute-force protector** (3 fails → 5-min lock) |
| **Logout / Token Revocation**     | `POST /api/v1/auth/logout` black-lists the JWT (token + exp stored in DB) so it can’t be reused             |
| **Profile Management**            | Retrieve, update, activate/deactivate & delete profiles with validation and optimistic locking              |
| **Training Management**           | CRUD trainings, list & filter by date-range, trainer/trainee name, and training type                        |
| **Trainer ⇄ Trainee assignments** | List unassigned active trainers · Atomic update of trainee’s trainer list                                   |
| **Monitoring & Health**           | Spring Actuator endpoints `/ops/prometheus`, `/ops/gym-health` exposed for Prometheus                       |
| **REST & Exception Handling**     | Global exception handler, consistent RFC 7807 (Problem-Details) responses                                   |
| **Documentation**                 | Swagger / OpenAPI with DTO validation examples & API metadata                                               |
| **Testing & Coverage**            | JUnit 5, Mockito; JaCoCo gate ≥ 70 % (uploaded to SonarQube)                                                |
| **Code Quality**                  | SonarQube static analysis; Spotless plugin with Google Java Format                                          |
| **Logging**                       | Console logs, transaction-ID filter, detailed REST call logging                                             |
| **Deployment**                    | Docker Compose stack: App, MySQL, Prometheus, Grafana, SonarQube – credentials via `.env`                   |

---

## 🛠️ Tech Stack 🛠️

| Layer             | Technology                                                                                                     |
| ----------------- | -------------------------------------------------------------------------------------------------------------- |
| Runtime           | **Java 21**, **Spring Boot 3.2.x**                                                                             |
| Security          | Spring Security 6 · OAuth2 Resource-Server (JWT) · BCrypt · Caffeine cache                                     |
| Persistence       | Spring Data JPA / Hibernate 6 · **MySQL 8**                                                                    |
| Monitoring        | Spring Boot **Actuator** · **Micrometer** · **Prometheus v2** · **Grafana 11** |
| Build             | **Maven 3.9.x** (wrapper) · Dockerfile / Docker Compose                                                        |
| Testing           | JUnit 5 · Mockito · Spring Boot Test · JaCoCo                                                                  |
| Documentation     | **Swagger / OpenAPI 3.1** (springdoc-openapi)                                                                  |
| Utilities         | Lombok · Jackson · Apache Commons Lang                                                                         |
| Quality & Linting | SonarQube 10 · Spotless (google-java-format)                                                                   |


---

## 🌐 URLS 🌐

http://localhost:8080/swagger-ui/index.html#/ <br>
http://localhost:9090 <br>
http://localhost:3000 <br>
http://localhost:9000 <br>

---

## 🚀 How to Run the App 🚀

### Command Line

| `docker compose up -d --build app`                          |
|------------------------------------|




<style>
  h1 { color: rgba(0,178,255,0.9); }
  h2 { color: #60c5db; }
  p  { color: rgb(255,255,255); }
</style>
