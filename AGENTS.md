# AGENTS.md

Project guidance for Codex when working in this repository.

## Project Overview

`live-test` is a Spring Boot 4.1.1 REST API template built with Gradle and
Java 17. It includes a user example domain, common API responses and errors,
OpenAPI generation, Testcontainers-based integration tests, and architecture
guardrails for live coding test work.

## Commands

- Build: `./gradlew build`
- Run the app: `./gradlew bootRun` (`.env.local` is loaded automatically when present)
- Run the app against a local dev-time Testcontainers MySQL instance: `./gradlew bootTestRun`
- Start local MySQL only: `docker compose --env-file .env.local -f docker-compose.local.yml up -d`
- Run the Docker/dev-profile app stack: `docker compose --env-file .env.dev -f docker-compose.dev.yml up --build`
- Expose the Docker/dev-profile app through ngrok when external callbacks or
  test clients need to reach it: `ngrok http 8080`
- Run all tests: `./gradlew test`
- Run a single test class: `./gradlew test --tests "com.ds.livetest.LiveTestApplicationTests"`
- Run a single test method: `./gradlew test --tests "com.ds.livetest.LiveTestApplicationTests.contextLoads"`
- Format code: `./gradlew spotlessApply`
- Check formatting: `./gradlew spotlessCheck`

Tests use JUnit 5 and Testcontainers. Docker must be running for tests or dev-time
runs that boot the Spring context with the MySQL container.

The app is intended to run locally, but it may receive external requests or call
external services during development. Treat ngrok URLs as temporary public
endpoints. Do not hardcode ngrok URLs, secrets, or external service endpoints in
source; wire them through profile configuration or environment variables when
features need them.

## Quality Rules

- Prefer the existing Gradle, Spring Boot, and package conventions before adding new patterns.
- Keep changes tightly scoped to the requested behavior.
- Run relevant tests after Java changes. Use focused test commands for narrow edits and
  `./gradlew test` or `./gradlew build` for broader changes.
- Use Spotless with Google Java Format for Java formatting. Run `./gradlew spotlessApply`
  only when formatting changes are intended.
- For Spring dependency injection, prefer constructor injection with Lombok
  `@RequiredArgsConstructor` and `final` fields. Declare constructors manually only
  when Lombok cannot express the required construction behavior.
- Error Prone runs on Java compilation and fails the build for ERROR-severity findings.
  Fix findings directly. If Error Prone false-positives on Lombok-generated members,
  disable the specific check on the affected compile task instead of disabling Error
  Prone globally.

## Architecture Notes

- Package root: `com.ds.livetest`.
- `LiveTestApplication` is the production entrypoint with `@SpringBootApplication`.
- `TestLiveTestApplication` in `src/test` is the dev-time entrypoint for `bootTestRun`.
  It starts `LiveTestApplication` with `TestcontainersConfiguration`.
- `TestcontainersConfiguration` defines the shared MySQL Testcontainers bean with
  `@ServiceConnection`, giving dev and test contexts an auto-wired MySQL `DataSource`
  without manual connection configuration.
- Dependencies already available for future work include Spring Data JPA, Bean
  Validation, and SpringDoc OpenAPI.
- `src/main/resources/application.yml` only contains the app name, default
  profile, and `spring.config.import` entries.
- Spring settings belong in `src/main/resources/config/*.yml`, split by one
  concern per file. Put profile differences in the same concern file with YAML
  document separators and `spring.config.activate.on-profile`.
- Local server settings enable forwarded header support so Spring can interpret
  proxy headers from ngrok.
- External HTTP integrations use Spring Boot HTTP Interface clients backed by
  `RestClient`. Shared timeout settings live in `config/http-client.yml`, and
  shared Resilience4j defaults live in `config/resilience.yml` under the
  `externalApi` instance.
- Cross-cutting external API support belongs under
  `com.ds.livetest.support.external`; actual domain-specific external clients
  should live near the feature that owns the integration.
- The default profile is `local`; supported profiles are `local`, `dev`, `prod`,
  and `test`. Test-only values belong in `src/test/resources/application-test.yml`.
- Environment placeholders must not define defaults. Use `${VAR}`, not
  `${VAR:default}`. When adding a new placeholder under main `config/*.yml`,
  update `.env.template` and `src/test/resources/application-test.yml` in the
  same change. If Docker AOT/cache training is introduced, update that training
  step in the same change as well.
- Architecture rules live in `docs/architecture.md`.
- Package creation and package-level `AGENTS.md` rules live in `docs/package-guidelines.md`.
- Testing rules live in `docs/testing.md`.
- Commit rules live in `docs/commit-guidelines.md`.
- Live test requirements live in `docs/requirements.md`; use it for pasted
  requirement text, domain rules, assumptions, open questions, and out-of-scope
  notes.
- Human-readable REST API contracts live in `docs/api-spec.md`; keep it aligned
  with `src/main/resources/openapi/openapi.yaml` when changing API behavior.
- Feature implementation notes live in `docs/implementation-notes.md`; use it
  for package planning, data flow, transaction notes, and test scenarios.
- Role-specific agent guidance lives in `docs/agents/`.
- When adding a new domain package, copy the matching templates from
  `docs/templates/package-agents/` into each new layer package as `AGENTS.md`.

## Role Guidance

Use role-specific guidance when the task matches its purpose. These documents
supplement this file and package-level `AGENTS.md` files; they do not replace
local package rules.

- Code review requests: use the `.codex/skills/code-review` skill; the source
  guidance also lives in `docs/agents/code-review.md`.
- Architecture, package layout, or dependency-rule changes: follow
  `docs/agents/architecture.md`.
- Test strategy, test implementation, or verification work: follow
  `docs/agents/testing.md`.
- REST API, controller, DTO, validation, or OpenAPI contract changes: follow
  `docs/agents/api-design.md`; keep `docs/api-spec.md` and
  `src/main/resources/openapi/openapi.yaml` aligned with the implemented
  contract.

## Codex Working Agreement

- Read the relevant code and configuration before changing behavior.
- When live test requirements are pasted in, first preserve the original text in
  `docs/requirements.md`, then summarize API contracts in `docs/api-spec.md`
  and implementation flow in `docs/implementation-notes.md` before making code
  changes.
- Read the relevant docs file before changing architecture, package layout, or tests.
- Follow `docs/commit-guidelines.md` when creating commits.
- Preserve user changes and avoid reverting unrelated files.
- Prefer small, direct implementations over speculative abstractions.
- Document only repo-specific facts and repeated workflow rules here; keep this file
  concise so it remains useful at the start of future Codex sessions.
