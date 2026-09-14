package com.ds.livetest.user;

import com.ds.livetest.support.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER-001", "이미 사용 중인 이메일입니다."),
  DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "USER-002", "이미 사용 중인 닉네임입니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  UserErrorCode(HttpStatus status, String code, String message) {
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
