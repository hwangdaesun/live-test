# Testing Guidelines

This project uses JUnit 5, Spring Boot Test, Testcontainers, Spotless, Error
Prone, and ArchUnit.

## Commands

- `./gradlew test`: run all tests, including Spring context and ArchUnit tests.
- `./gradlew test --tests "com.ds.livetest.ArchitectureTest"`: run architecture
  rules only.
- `./gradlew spotlessCheck`: verify formatting.
- `./gradlew build`: run compilation, checks, and packaging.

Docker must be running for tests that boot the Spring context with the
Testcontainers MySQL configuration.

## Test Placement

Mirror production packages under `src/test/java`. Name test classes after the
unit under test, for example `MemberServiceTest` or `MemberControllerTest`.

## Integration Tests

Write Spring integration tests by extending `IntegrationTestSupport`.

`IntegrationTestSupport` starts the application with
`@SpringBootTest(webEnvironment = RANDOM_PORT)` and imports the shared
Testcontainers MySQL configuration. Use it for API-level integration tests and
for service/repository tests that need the real Spring context and database.

The base class clears MySQL data before each test through `DatabaseCleaner`.
The cleaner truncates all ordinary tables in the current schema while preserving
known migration metadata tables.

## Test Fixtures

Create mock and test data through `TestFixtures` instead of building long object
graphs inline in test methods. Keep fixture methods small and named after the
state they create. When a domain grows large, split fixtures into domain-specific
classes under `src/test/java/com/ds/livetest/support/fixture`.

## Architecture Tests

`ArchitectureTest` enforces package dependency rules. Add new architecture rules
when package conventions become important enough to protect automatically.

When changing layer rules, update all of these together:

- `docs/architecture.md`
- `docs/package-guidelines.md`
- `src/test/java/com/ds/livetest/ArchitectureTest.java`
- affected package-level `AGENTS.md` templates
