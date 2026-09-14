package com.ds.livetest.support.error;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-000", "예상하지 못한 오류가 발생했습니다."),
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 입력입니다."),
  NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "COMMON-002", "요청한 리소스를 찾을 수 없습니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-003", "지원하지 않는 HTTP 메서드입니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON-004", "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON-005", "접근 권한이 없습니다."),
  EXTERNAL_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "COMMON-006", "외부 API 요청이 올바르지 않습니다."),
  UNSUPPORTED_MEDIA_TYPE(
      HttpStatus.UNSUPPORTED_MEDIA_TYPE, "COMMON-007", "지원하지 않는 Content-Type입니다."),
  NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "COMMON-008", "지원하지 않는 응답 형식입니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  CommonErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  @Override
  public HttpStatus status() {
    return status;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String message() {
    return message;
  }
}
