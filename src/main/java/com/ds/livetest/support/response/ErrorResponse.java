package com.ds.livetest.support.response;

import com.ds.livetest.support.error.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@Schema(name = "ErrorResponse")
public record ErrorResponse(
    String code, String message, List<FieldErrorResponse> fieldErrors, Instant timestamp) {

  public static ErrorResponse from(ErrorCode errorCode) {
    return from(errorCode, List.of());
  }

  public static ErrorResponse from(ErrorCode errorCode, List<FieldErrorResponse> fieldErrors) {
    return new ErrorResponse(errorCode.code(), errorCode.message(), fieldErrors, Instant.now());
  }
}
