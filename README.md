# Frostbyte
Frostbyte is a Spring Boot–based case project structured as a small microservice landscape. It currently includes:

- **Gateway** – entrypoint for routing traffic to backend services.
- **User-service** – planned service for managing users and authentication (with passkey support on the roadmap).
- **Check-service** – planned service for recording or evaluating user checks.

The repo is set up for local development with Maven and Docker Compose (see `Instructions.md` for details). Each service lives in its own module directory and can be started individually with the provided Maven profile.
