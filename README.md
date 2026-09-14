# live-test

Spring Boot 기반 REST API 라이브 코딩 테스트를 빠르게 시작하기 위한 템플릿입니다.
Java 17, Spring Boot 4.1.1, Gradle을 사용하며 사용자 예제, 공통 API 응답,
OpenAPI 계약, Testcontainers 통합 테스트, 아키텍처 가드레일을 미리 구성해 두었습니다.

이 저장소는 단순한 샘플 앱이 아니라, 제한된 시간 안에서 요구사항을 받고 AI와
협업해 안정적으로 구현하기 위한 작업대에 가깝습니다.

## 주요 구성

- Spring Web MVC, Spring Data JPA, Bean Validation
- 사용자 API 예제
- 공통 성공/에러 응답 모델
- SpringDoc UI와 OpenAPI Generator 기반 API 인터페이스 생성
- MySQL Testcontainers 기반 테스트 및 dev-time 실행
- ArchUnit으로 보호하는 패키지 의존성 규칙
- Spotless와 Error Prone 기반 코드 품질 검사
- Docker Compose와 ngrok을 통한 외부 테스트 환경

## 빠른 실행

```bash
./gradlew bootRun
```

`bootRun`은 `.env.local`이 있으면 자동으로 읽습니다. 로컬에서 MySQL이 필요하면
먼저 다음 명령으로 데이터베이스만 띄웁니다.

```bash
docker compose --env-file .env.local -f docker-compose.local.yml up -d
```

Testcontainers가 제공하는 MySQL과 함께 앱을 실행하려면 다음 명령을 사용합니다.

```bash
./gradlew bootTestRun
```

Docker dev 프로필 전체 스택은 `8080` 포트로 실행됩니다.

```bash
docker compose --env-file .env.dev -f docker-compose.dev.yml up --build
```

외부 콜백이나 테스트 클라이언트가 접근해야 할 때는 dev 스택을 실행한 뒤 ngrok을
연결합니다.

```bash
ngrok http 8080
```

## 검증 명령

```bash
./gradlew test
./gradlew build
./gradlew spotlessCheck
```

단일 테스트를 실행할 때는 Gradle의 `--tests` 옵션을 사용합니다.

```bash
./gradlew test --tests "com.ds.livetest.LiveTestApplicationTests"
```

테스트와 dev-time Testcontainers 실행에는 Docker가 필요합니다.

## AI 활용 세팅

이 프로젝트는 Codex 같은 AI 코딩 에이전트가 매번 같은 기준으로 움직이도록
저장소 안에 작업 규칙을 넣어 두었습니다.

- `AGENTS.md`: 프로젝트 전체 명령, 품질 기준, 패키지 규칙, 문서 갱신 규칙을
  정의합니다.
- `docs/requirements.md`: 라이브 테스트 요구사항 원문, 해석, 가정, 제외 범위를
  보존하는 문서입니다.
- `docs/api-spec.md`: 사람이 읽는 REST API 계약 문서입니다. API 동작을 바꾸면
  `src/main/resources/openapi/openapi.yaml`과 함께 갱신합니다.
- `docs/implementation-notes.md`: 요구사항을 코드로 옮기기 위한 패키지 계획,
  데이터 흐름, 트랜잭션, 테스트 시나리오를 정리합니다.
- `docs/agents/`: API 설계, 테스트, 아키텍처, 코드 리뷰처럼 역할별로 다른
  판단 기준을 둡니다.
- 패키지별 `AGENTS.md`: `presentation`, `service`, `repository`, `domain`
  레이어 가까이에 로컬 규칙을 둬서 AI가 파일을 열었을 때 맥락을 놓치지 않게
  합니다.
- `.codex/skills/code-review`: 변경 사항을 버그, 회귀, 테스트 누락,
  아키텍처 위반 중심으로 리뷰하도록 만든 저장소 전용 스킬입니다.
- `.codex/skills/live-test-dev-tunnel`: dev Docker Compose 스택을 `8080`으로
  띄우고 ngrok으로 노출하는 반복 작업을 표준화한 스킬입니다.

라이브 테스트 요구사항을 받으면 먼저 원문을 `docs/requirements.md`에 보존하고,
API 계약은 `docs/api-spec.md`, 구현 흐름은 `docs/implementation-notes.md`에
정리한 뒤 코드를 수정하는 흐름을 기본으로 삼습니다.

## 개발 규칙 요약

- 패키지는 도메인 우선, 레이어 후순위로 나눕니다.
  `com.ds.livetest.<domain>.presentation`, `service`, `repository`, `domain`
  형태를 사용합니다.
- 의존성 흐름은 `presentation -> service -> repository -> domain`을 따릅니다.
- Spring 설정은 `src/main/resources/config/*.yml`에 관심사별로 분리합니다.
- main 설정의 환경변수 플레이스홀더에는 기본값을 넣지 않습니다. `${VAR}` 형식을
  사용하고, 새 환경변수를 추가하면 `.env.template`과 테스트 설정도 함께
  갱신합니다.
- 컨트롤러, DTO, 검증, OpenAPI 계약을 바꿀 때는 `docs/agents/api-design.md`의
  기준을 따릅니다.
- 테스트나 검증 전략을 바꿀 때는 `docs/agents/testing.md`와
  `docs/testing.md`를 먼저 확인합니다.
- 새 도메인 패키지를 만들면 `docs/templates/package-agents/`의 템플릿을 각
  레이어 패키지의 `AGENTS.md`로 복사합니다.

## 문서 지도

- `docs/pre-test-setup.md`: 라이브 테스트 전 환경 점검 체크리스트
- `docs/architecture.md`: 패키지 구조와 의존성 규칙
- `docs/testing.md`: 테스트 작성 및 실행 기준
- `docs/package-guidelines.md`: 새 패키지 생성 규칙
- `docs/commit-guidelines.md`: 커밋 작성 기준
- `requests.http`: 로컬, Docker, ngrok 환경에서 빠르게 API를 확인하는 요청 모음

## 참고 링크

- [Spring Boot Gradle Plugin](https://docs.spring.io/spring-boot/4.1.1/gradle-plugin)
- [Spring Boot Testcontainers](https://docs.spring.io/spring-boot/4.1.1/reference/testing/testcontainers.html)
- [Testcontainers MySQL](https://java.testcontainers.org/modules/databases/mysql/)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [Gradle](https://docs.gradle.org)
