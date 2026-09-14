# Domain Package Guidelines

This package contains the core model for one domain.

## Responsibilities

- Define entities, value objects, enums, and domain rules.
- Keep business invariants close to the data they protect.
- Prefer meaningful business names over technical names.

## Dependency Rules

- Must not depend on `presentation`, `service`, or `repository`.
- Avoid Spring web and persistence infrastructure unless an intentional project
  rule says otherwise.
- Keep domain behavior usable without HTTP or database code.

## Naming

- Entities and value objects use business terms, for example `Member` or
  `EmailAddress`.
- Domain exceptions should describe the violated business rule.
