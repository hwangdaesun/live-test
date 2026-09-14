# Commit Guidelines

Use small, focused commits. One commit must represent one coherent task.

## Format

Commit messages must use this format:

```text
$operator($domain): $message
```

Examples:

```text
feat(auth): 로그인 API 추가
fix(sticker): 스티커 조회 조건 수정
docs(analysis): 분석 도메인 규칙 문서화
```

## Operator

`$operator` must be one of:

- `feat`: add or expand user-facing behavior
- `fix`: correct a bug
- `refactor`: change structure without changing behavior
- `chore`: maintenance work
- `test`: add or update tests
- `docs`: add or update documentation
- `style`: formatting-only changes
- `ci`: CI or build pipeline changes

## Domain

`$domain` is the affected domain package name, such as `analysis`, `auth`, or
`sticker`.

Use the smallest accurate domain. For repository-wide maintenance that does not
belong to a business domain, use a concise technical domain such as `build`,
`docs`, or `architecture`.

## Message

Write `$message` in Korean. Keep it short and action-oriented.

Good:

```text
refactor(architecture): 계층 의존 규칙 정리
```

Avoid vague messages:

```text
chore(docs): 작업
```

## Trailers

Do not add trailers. This includes tool/session trailers such as
`Claude-Session`.

## Commit Size

Split work by task:

- One commit for one behavior, rule, or cleanup.
- Separate documentation-only changes from code changes when they are independent.
- Separate refactors from behavior changes unless the refactor is required for the
  same task.
