# Backend (Spring Boot)

## Requirements
- Java 17+
- Maven 3.8+

## Run
1. cd backend
2. mvn spring-boot:run

H2 console available at http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:votingdb)

API endpoints:
- POST /api/auth/register  {username,password,role}
- POST /api/auth/login     {username,password} -> {token}
- GET  /api/elections
- GET  /api/elections/{id}/candidates
- POST /api/elections/{id}/vote  (Authorization: Bearer <token>) body {candidateId}
- GET  /api/elections/{id}/results

Note: seed endpoint is intentionally minimal.
