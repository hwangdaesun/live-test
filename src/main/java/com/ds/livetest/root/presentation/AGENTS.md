# Presentation Package Guidelines

This package contains the HTTP boundary for one domain.

## Responsibilities

- Define controllers, request DTOs, response DTOs, and web validation.
- Delegate business work to the domain's `service` package.
- Keep HTTP concerns out of `service`, `repository`, and `domain`.

## Dependency Rules

- May depend on this domain's `service` package.
- May depend on this domain's `domain` package for response mapping when useful.
- Must not depend on `repository` directly.

## Naming

- Controllers end with `Controller`.
- HTTP DTOs stay in this presentation package.
- Request DTOs end with `Request`; response DTOs end with `Response`.
