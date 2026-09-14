package com.ds.livetest.support.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponse")
public class ApiResponse<T> {

  private final boolean success;

  @JsonInclude(JsonInclude.Include.ALWAYS)
  private final T data;

  @JsonInclude(JsonInclude.Include.ALWAYS)
  private final ErrorResponse error;

  protected ApiResponse(boolean success, T data, ErrorResponse error) {
    this.success = success;
    this.data = data;
    this.error = error;
  }

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(true, data, null);
  }

  public static ApiResponse<Void> error(ErrorResponse error) {
    return new ApiResponse<>(false, null, error);
  }

  public boolean isSuccess() {
    return success;
  }

  public T getData() {
    return data;
  }

  public ErrorResponse getError() {
    return error;
  }
}
