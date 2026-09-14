# Architecture Rules

This project uses a package-based DDD lite structure with a conventional
3-layer architecture. Production code belongs under `com.ds.livetest`.
Cross-cutting support code may live under `com.ds.livetest.support` when it is
infrastructure-oriented and independent from domain layer packages.

## Package Shape

Group code by domain first, then by layer:

```text
com.ds.livetest.<domain>.presentation
com.ds.livetest.<domain>.service
com.ds.livetest.<domain>.repository
com.ds.livetest.<domain>.domain
com.ds.livetest.support.external
```

Example for a `member` domain:

```text
com.ds.livetest.member.presentation
com.ds.livetest.member.service
com.ds.livetest.member.repository
com.ds.livetest.member.domain
```

## Layer Responsibilities

- `presentation`: HTTP controllers, request DTOs, response DTOs, and web validation.
- `service`: use-case services and business flow orchestration.
- `repository`: persistence implementations, Spring Data adapters, and JPA queries.
- `domain`: entities, value objects, enums, and domain rules.
- `support.external`: shared external API client configuration, resilience
  support, exceptions, and adapter helpers.

## Dependency Rules

Allowed dependencies:

```text
presentation -> service -> repository -> domain
service -> domain
```

Forbidden dependencies:

- `domain` must not depend on `presentation`, `service`, or `repository`.
- `repository` must not depend on `presentation` or `service`.
- `service` must not depend on `presentation`.
- `presentation` must not depend on `repository` directly.
- `support.external` must not depend on `presentation`, `service`,
  `repository`, or `domain`.

ArchUnit tests enforce these rules. If the architecture intentionally changes,
update `ArchitectureTest`, this document, and the package-level `AGENTS.md`
templates in the same change.
