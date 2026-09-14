# API Spec

구현할 REST API 계약을 사람 기준으로 먼저 정리한다. 최종 계약은
`src/main/resources/openapi/openapi.yaml`에도 반영한다.

## API List

| Method | Path | Auth | Purpose |
| --- | --- | --- | --- |
| GET | `/health` | 필요 없음 | 서비스 정상 동작 확인 |
| POST | `/api/vote` | 필요 없음 | 짜장면/짬뽕 투표 등록 |
| GET | `/api/result` | 필요 없음 | 현재 투표 결과 조회 |

## Endpoints

### 헬스 체크: `GET /health`

목적:

- 클라이언트가 서비스 정상 동작 여부를 확인한다.

인증:

- 필요 없음

Request:

- 없음

Validation:

- 없음

Success Response:

- Status: `200 OK`

```json
{
  "success": true,
  "data": "ok",
  "error": null
}
```

Error Responses:

- 없음

### 투표하기: `POST /api/vote`

목적:

- 클라이언트가 `jajang` 또는 `jjamppong` 중 하나를 선택해 투표한다.

인증:

- 필요 없음

Request:

```json
{
  "choice": "jajang",
  "voterId": "user-123"
}
```

Validation:

- `choice`: 필수, `jajang` 또는 `jjamppong`만 허용
- `voterId`: 필수, blank 불가, 최대 100자
- `voterId`는 이 서비스의 `userId`와 같은 값으로 취급

Success Response:

- Status: `201 Created`

```json
{
  "success": true,
  "data": {
    "id": "0191c8a2-66f9-7d49-940f-a96f6e0bd013",
    "choice": "jajang",
    "voterId": "user-123"
  },
  "error": null
}
```

Error Responses:

| Status | Code | Condition |
| --- | --- | --- |
| 400 | `COMMON-001` | `choice` 누락/잘못된 값, `voterId` 누락/blank/길이 초과, malformed JSON |
| 409 | `VOTE-001` | 이미 투표한 `voterId`로 다시 요청 |

Notes:

- `choice`는 대소문자를 구분하며 소문자 `jajang`, `jjamppong`만 허용한다.
- 동일 `voterId`는 동일 `userId`로 간주하며 한 번만 투표할 수 있다.

### 현재 투표 결과 조회: `GET /api/result`

목적:

- 클라이언트가 현재까지 저장된 투표 결과를 조회한다.

인증:

- 필요 없음

Request:

- 없음

Validation:

- 없음

Success Response:

- Status: `200 OK`

```json
{
  "success": true,
  "data": {
    "jajang": 120,
    "jjamppong": 95,
    "total": 215
  },
  "error": null
}
```

Error Responses:

| Status | Code | Condition |
| --- | --- | --- |
| 500 | `COMMON-000` | 예상하지 못한 서버/DB 오류 |

Notes:

- 결과는 투표 성공 transaction에서 즉시 갱신한 `vote_statistics` 테이블의 `jajang`, `jjamppong` row를 기준으로 반환한다.
- 투표 성공 직후 결과에 즉시 반영된다.
- 없는 통계 row는 `0`으로 취급한다.
- `total`은 `jajang + jjamppong`으로 계산한다.

## Contract Checklist

- [x] endpoint path와 HTTP method를 정했다.
- [x] request body, query parameter, path variable을 정했다.
- [x] validation 규칙을 정했다.
- [x] 성공 status code와 response body를 정했다.
- [x] 예상 가능한 실패 status code와 error code를 정했다.
- [x] 인증 필요 여부를 정했다.
- [x] `openapi.yaml` 반영 범위를 확인했다.
- [x] API 레벨 테스트 시나리오를 정했다.
