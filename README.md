# 💠 GYM TASK APPLICATION – REST EDITION 💠


The **Gym Task** project is a robust Spring Boot + Hibernate REST API application. It manages Trainers, Trainees, bi-directional Trainer ↔ Trainee assignments, Trainings, authentication, profile lifecycle, error handling, documentation with Swagger, and more, ensuring security, transaction management, and comprehensive validation.

---

## ✨ Key Features ✨

| Area | Endpoints / Use-Cases                                                                            |
|------|--------------------------------------------------------------------------------------------------|
| **Account Creation** | Trainer & Trainee Registration with auto-generated credentials.                                  |
| **Authentication** | Login validation, Change Password.                                                               |
| **Profile Management** | Retrieve, update, activate/deactivate, and delete profiles with validation.                      |
| **Training Management** | List and add trainings with filtering options (date-range, trainer/trainee name, training type). |
| **Trainer ⇄ Trainee assignments** | List unassigned active trainers, update trainee’s assigned trainers list atomically.             |
| **REST & Exception Handling** | Global exception handler, custom request exceptions.                                             |
| **Documentation** | Swagger/OpenAPI documentation with DTOs validations, examples, and API metadata.                 |
| **Testing & Coverage** | JUnit 5, Mockito, JaCoCo enforcing minimum 80% coverage.                                         |
| **Code Quality** | SonarQube static code analysis, Spotless plugin with Google Java Format.                         |
| **Logging** | Console logging (default), transaction-level logging, REST call details logging.                 |
| **Deployment** | Dockerfiles provided for SQL DB setup and easy local application deployment with secure handling of credentials via environment variables.                                     |
---

## 🛠️ Tech Stack 🛠️

| Layer | Technology                                       |
|-------|--------------------------------------------------|
| Runtime | **Java 21**, **Spring Boot 3.2.x**               |
| Persistence | **Spring Data JPA / Hibernate 6**, **MySQL 8**   |
| Build | **Maven 3.9.x**                                  |
| Testing | JUnit 5 · Mockito · Spring Boot Test · JaCoCo    |
| Documentation | Swagger/OpenAPI    |
| Utility | Lombok · Jackson · Apache Commons Lang |
| Quality & Linting | SonarQube, Spotless (Google Java Format)   |

---

## ⚙️ Configuration ⚙️

### Environment Variables

| Property | Default ⏎ | Description |
|----------|-----------|-------------|
| `DB_URL` | `jdbc:mysql://localhost:3306/gymapp` | JDBC URL |
| `DB_USERNAME` | `gymuser` | DB username |
| `DB_PASSWORD` | *(none)* | DB password |





<style>
  h1 { color: rgba(0,178,255,0.9); }
  h2 { color: #60c5db; }
  p  { color: rgb(255,255,255); }
</style>
