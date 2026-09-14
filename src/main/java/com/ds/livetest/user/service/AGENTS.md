# Service Package Guidelines

This package contains use-case services for one domain.

## Responsibilities

- Orchestrate business flows and transaction boundaries.
- Use domain models for business concepts.
- Call repositories when persistence is needed.

## Dependency Rules

- May depend on this domain's `domain` and `repository` packages.
- Must not depend on `presentation`.
- Keep HTTP DTOs and web annotations out of this package.

## Naming

- Application services end with `Service`.
- Use method names that describe use cases, not transport details.
