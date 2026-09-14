package com.ds.livetest.support.error;

public class UnauthorizedException extends BusinessException {

  public UnauthorizedException(ErrorCode errorCode) {
    super(errorCode);
  }
}
