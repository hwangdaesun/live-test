package com.ds.livetest.support.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

  HttpStatus status();

  String code();

  String message();
}
