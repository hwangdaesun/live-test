# Package Guidelines

Use domain-first packages for production code. Do not create root-level layer
packages such as `com.ds.livetest.service` or `com.ds.livetest.repository`.
Cross-cutting infrastructure support may live under `com.ds.livetest.support`.

## Creating a Domain

When adding a new domain, create only the layers needed for the feature:

```text
src/main/java/com/ds/livetest/<domain>/presentation
src/main/java/com/ds/livetest/<domain>/service
src/main/java/com/ds/livetest/<domain>/repository
src/main/java/com/ds/livetest/<domain>/domain
```

Copy the matching template from `docs/templates/package-agents/` into each layer
directory as `AGENTS.md`. Keep package-level instructions short and specific to
that layer.

## Naming

- Controllers end with `Controller`, for example `MemberController`.
- Services end with `Service`, for example `MemberService`.
- Repository implementations or Spring Data repositories end with `Repository`.
- Request DTOs end with `Request`; response DTOs end with `Response`.
- HTTP DTOs are presentation-layer types and belong in the owning
  `presentation` package.
- Domain entities and value objects use business names, for example `Member` or
  `EmailAddress`.

## Placement Rules

- Put web annotations and HTTP DTOs in `presentation`.
- Put transaction boundaries and use-case orchestration in `service`.
- Put JPA queries, persistence adapters, and database-specific mappings in
  `repository`.
- Put invariants and domain behavior in `domain`.

If a class does not clearly belong to one layer, pause and clarify the boundary
before adding it.

## External API Support

Use `com.ds.livetest.support.external` only for external API infrastructure:
HTTP client configuration, shared resilience behavior, exceptions, and adapter
helpers. Do not put domain-specific client contracts there. Place actual
external API interfaces beside the feature that owns the integration, usually
in that domain's repository or adapter boundary, and apply the shared
`externalApi` resilience defaults unless a stricter service-specific policy is
needed.
