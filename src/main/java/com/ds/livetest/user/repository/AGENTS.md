# Repository Package Guidelines

This package contains persistence details for one domain.

## Responsibilities

- Implement database access with Spring Data JPA or persistence adapters.
- Keep query and database-specific mapping details here.
- Return or persist domain objects according to service needs.

## Dependency Rules

- May depend on this domain's `domain` package.
- Must not depend on `service` or `presentation`.
- Keep web DTOs and controller concerns out of this package.

## Naming

- Repositories end with `Repository`.
- Custom persistence helpers should include persistence-specific names such as
  `Jpa`, `Query`, or `Mapper` when that clarifies intent.
