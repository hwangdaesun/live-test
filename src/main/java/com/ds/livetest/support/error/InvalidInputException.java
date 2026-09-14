package com.ds.livetest.support.error;

public class InvalidInputException extends BusinessException {

  public InvalidInputException(ErrorCode errorCode) {
    super(errorCode);
  }
}
