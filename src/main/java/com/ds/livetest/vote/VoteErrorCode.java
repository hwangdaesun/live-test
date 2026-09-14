package com.ds.livetest.vote;

import com.ds.livetest.support.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum VoteErrorCode implements ErrorCode {
  DUPLICATE_VOTER(HttpStatus.CONFLICT, "VOTE-001", "이미 투표한 사용자입니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  VoteErrorCode(HttpStatus status, String code, String message) {
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
