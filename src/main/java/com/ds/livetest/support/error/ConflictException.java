package com.ds.livetest.support.error;

public class ConflictException extends BusinessException {

  public ConflictException(ErrorCode errorCode) {
    super(errorCode);
  }
}
