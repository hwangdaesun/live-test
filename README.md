# live-test

Spring Boot 기반 REST API 라이브 코딩 테스트를 빠르게 시작하기 위한 템플릿입니다.
Java 17, Spring Boot 4.1.1, Gradle을 사용하며 사용자 예제, 공통 API 응답,
OpenAPI 계약, Testcontainers 통합 테스트, 아키텍처 가드레일을 미리 구성해 두었습니다.

이 저장소는 단순한 샘플 앱이 아니라, 제한된 시간 안에서 요구사항을 받고 AI와
협업해 안정적으로 구현하기 위한 작업대에 가깝습니다.

## 주요 구성

- Spring Web MVC, Spring Data JPA, Bean Validation
- 사용자 API 예제
- 짜장면/짬뽕 투표 API와 결과 조회 API
- `voterId` 기반 중복 투표 방지
- MySQL `votes`, `vote_statistics` 기반 투표 저장과 집계
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

`bootRun`은 `.env.local`이 있으면 자동으로 읽습니다. 로컬 앱은 `8090` 포트로
실행됩니다. 로컬에서 MySQL이 필요하면 먼저 다음 명령으로 데이터베이스만
띄웁니다.

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

## 투표 API 빠른 확인

투표 등록은 `POST /api/vote`를 사용합니다. `choice`는 `jajang` 또는
`jjamppong`만 허용하고, `voterId`는 이 서비스에서 `userId`와 같은 값으로
취급합니다.

```bash
curl -X POST http://localhost:8090/api/vote \
  -H 'Content-Type: application/json' \
  -d '{"choice":"jajang","voterId":"user-123"}'
```

성공하면 `201 Created`와 생성된 투표 정보를 반환합니다. 같은 `voterId`로 다시
투표하면 `409 Conflict`와 `VOTE-001` 오류를 반환합니다.

현재 결과는 `GET /api/result`로 조회합니다.

```bash
curl http://localhost:8090/api/result
```

응답 예시는 다음과 같습니다.

```json
{
  "success": true,
  "data": {
    "jajang": 1,
    "jjamppong": 0,
    "total": 1
  },
  "error": null
}
```

결과 조회는 `vote_statistics` 테이블을 기준으로 합니다. 현재 구현은 투표 성공
transaction 안에서 해당 선택지의 통계를 즉시 증가시킵니다.

## 구현 정리

### 실행 방법

- 로컬 실행은 `./gradlew bootRun`을 사용합니다. 로컬 앱은 `8090` 포트로 실행됩니다.
- 로컬 MySQL이 필요하면 `docker-compose.local.yml`로 MySQL만 먼저 실행합니다.
- Testcontainers 기반으로 앱을 실행하려면 `./gradlew bootTestRun`을 사용합니다.
- Docker dev 스택은 `docker-compose.dev.yml`로 실행하며, 앱은 `8080` 포트로 노출됩니다.

### 사용한 기술

- Java 17, Spring Boot 4.1.1, Gradle
- Spring Web MVC, Spring Data JPA, Bean Validation
- MySQL, Testcontainers, Docker Compose
- OpenAPI Generator, SpringDoc UI
- ngrok

### 데이터 저장 방식

- `votes` 테이블은 개별 투표 원장으로 사용합니다.
- `vote_statistics` 테이블은 결과 조회용 집계 테이블로 사용합니다.
- 투표 성공 시 같은 transaction 안에서 `votes`에 투표를 저장하고, 해당 선택지의
  `vote_statistics.vote_count`를 증가시킵니다.
- `GET /api/result`는 `vote_statistics`의 `jajang`, `jjamppong` row를 기준으로
  결과를 반환합니다.

### 동시성 및 중복 투표 처리 방식

- 중복 투표 기준은 `voterId`입니다. 이 서비스에서 `voterId`는 `userId`와 같은
  사용자 식별자로 취급합니다.
- 이미 존재하는 `voterId`는 `PESSIMISTIC_WRITE` 비관적 락으로 조회해 같은 사용자의
  재투표를 차단합니다.
- 동시에 같은 `voterId`로 신규 요청이 들어오는 경우를 대비해
  `votes.voter_id` unique key를 최종 방어선으로 둡니다.
- 중복 투표로 판단되면 `409 Conflict`와 `VOTE-001` 오류를 반환합니다.
- Redis pre-filter로 한 사용자당 한 번만 투표 가능하게 선차단하는 방식은 예정
  사항이며, 아직 구현하지 않았습니다.

### 재시작 후 데이터 유지 방식

- 투표 원장과 집계는 MySQL에 저장하므로 애플리케이션을 재시작해도 데이터는 DB 기준으로
  유지됩니다.
- Docker Compose 환경에서 컨테이너 재생성 후에도 데이터를 유지하려면 MySQL volume 또는
  외부 DB 연결을 사용해야 합니다.
- 트래픽 확장 구조에서 queue를 사용한다면, 처리되지 않은 투표 이벤트를 queue에 보관해
  애플리케이션 재시작 이후에도 재처리할 수 있도록 설계합니다.

### Public URL 구성 방식

- 외부 테스트가 필요할 때는 Docker dev 스택을 `8080` 포트로 실행한 뒤 ngrok을 연결합니다.

```bash
ngrok http 8080
```

- ngrok이 발급한 public URL을 테스트 클라이언트나 `requests.http`의 base URL로 사용합니다.

### 중요하게 판단한 설계 사항

- 동시성 제어 방식은 비관적 락과 unique key 조합을 선택했습니다. 이미 저장된
  `voterId`는 락으로 보호하고, 아직 row가 없어 잠글 대상이 없는 신규 동시 요청은 DB
  unique key로 한 번 더 막기 위함입니다.
- MySQL을 source of truth로 두었습니다. Redis 같은 캐시는 성능 최적화와 선차단 용도로
  사용할 수 있지만, 최종 중복 판단과 데이터 복구 기준은 DB가 담당하는 구조가 안전하다고
  판단했습니다.
- 트래픽이 크게 몰리는 상황이라면 Redis pre-filter로 중복 요청을 먼저 거르고, queue로
  투표 저장 부하를 완충한 뒤, MySQL 원장을 기준으로 스케줄링 집계를 업데이트하는 방식으로
  확장할 수 있습니다.
- 결과가 즉시 보여야 하는 요구사항이라면 현재처럼 투표 저장 transaction 안에서 통계를 함께
  증가시키는 방식이 단순합니다. 즉시 반영이 필수가 아니라면 투표 저장 후 이벤트를 발행하고,
  이벤트 핸들러가 `vote_statistics`에 반영하는 구조를 고려할 수 있습니다. 더 큰 트래픽에서는
  MySQL 원장을 source of truth로 두고, 스케줄러가 주기적으로 원장 기준 집계를 재계산해 통계
  테이블을 갱신하는 방식도 선택할 수 있습니다.

## 검증 명령

```bash
./gradlew test
./gradlew build
./gradlew spotlessCheck
```

단일 테스트를 실행할 때는 Gradle의 `--tests` 옵션을 사용합니다.

```bash
./gradlew test --tests "com.ds.livetest.LiveTestApplicationTests"
./gradlew test --tests "com.ds.livetest.vote.presentation.VoteControllerTest"
```

테스트와 dev-time Testcontainers 실행에는 Docker가 필요합니다.

## AI 활용 세팅

이 프로젝트는 Codex 같은 AI 코딩 에이전트가 매번 같은 기준으로 움직이도록
저장소 안에 작업 규칙을 넣어 두었습니다.

- `AGENTS.md`: 프로젝트 전체 명령, 품질 기준, 패키지 규칙, 문서 갱신 규칙을
  정의합니다.
- `docs/requirements.md`: 라이브 테스트 요구사항 원문, 해석, 가정, 제외 범위를
  보존하는 문서입니다.
- `docs/api-spec.md`: 사람이 읽는 REST API 계약 문서입니다. 투표 API 요청,
  응답, 검증, 오류 계약도 여기서 확인합니다. API 동작을 바꾸면
  `src/main/resources/openapi/openapi.yaml`과 함께 갱신합니다.
- `docs/implementation-notes.md`: 요구사항을 코드로 옮기기 위한 패키지 계획,
  투표 저장/집계 데이터 흐름, 트랜잭션, 테스트 시나리오를 정리합니다.
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
