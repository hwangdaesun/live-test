package com.ds.livetest.support.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponse")
public final class ApiEnvelope extends ApiResponse<Object> {

  private ApiEnvelope(boolean success, Object data, ErrorResponse error) {
    super(success, data, error);
  }

  public static ApiEnvelope ofSuccess(Object data) {
    return new ApiEnvelope(true, data, null);
  }
}
