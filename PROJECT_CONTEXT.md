# Virtual Gamer Project Handover

## Infrastructure

### VPS

* Ubuntu 24.04 VPS
* Domain: virtualgamer.co.uk
* API: api.virtualgamer.co.uk
* Nginx reverse proxy in front of Spring Boot API
* PostgreSQL running in Docker

### Backend Repository

* Repo: FirstServerAPI
* Java: 25
* Docker image: eclipse-temurin:25-jdk
* Spring Boot backend
* Docker Compose deployment

### Frontend Repository

* Repo: virtualgamer-frontend
* React + Vite
* Hosted via Nginx
* CI/CD via GitHub Actions

---

## ARCHITECTURAL STANDARDS (Added June 2026)

### Development Methodology

The project follows Test Driven Development (TDD):

1. RED - Write failing tests
2. GREEN - Implement minimum code required
3. REFACTOR - Improve design while keeping tests green

New features should follow this workflow whenever practical.

### Controller Standards

Controllers must remain thin.

Controllers should:
- Validate requests
- Delegate business logic to services
- Return DTOs

Controllers should not:
- Contain business logic
- Access repositories directly
- Return JPA entities

### DTO Standards

JPA entities must never be exposed directly through REST APIs.

Pattern:

Controller
→ Request DTO
→ Service
→ Entity
→ Mapper
→ Response DTO

DTOs should be used for all API boundaries.

Java Records are preferred for DTOs where appropriate.

### Service Standards

Business rules belong in services.

Repositories are persistence-only components.

### Lombok Standards

Lombok is approved for reducing boilerplate.

Preferred usage:
- @Getter
- @Setter
- @Builder
- @NoArgsConstructor
- @AllArgsConstructor

Avoid @Data on JPA entities.

### Testing Standards

Feature work should include:
- Service tests
- Repository tests
- Controller tests
- Mapper tests

Spock remains the primary testing framework.

### Decision Log

June 2026:
- Adopt TDD-first development workflow.
- Controllers return DTOs only.
- Use mapper layer between entities and DTOs.
- Adopt Lombok to reduce boilerplate.
- Continue Spring Boot, JPA, Security and Spock as primary framework stack.

### Planned Chat System Architecture

Messaging will use a unified chat model.

Core entities:
- ChatRoomEntity
- ChatMemberEntity
- ChatMessageEntity
- ChatInviteEntity

Chat types:
- DIRECT
- GROUP

Single architecture supports:
- One-to-one messaging
- Group chats
- Invitations
- Membership management
- Future WebSocket real-time messaging

---

(Existing project context retained below)
