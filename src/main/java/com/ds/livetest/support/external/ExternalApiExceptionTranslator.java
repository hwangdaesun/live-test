package com.ds.livetest.support.external;

import com.ds.livetest.support.error.CommonErrorCode;
import com.ds.livetest.support.error.InvalidInputException;
import org.springframework.web.client.RestClientResponseException;

public final class ExternalApiExceptionTranslator {

  private ExternalApiExceptionTranslator() {}

  public static RuntimeException translate(RestClientResponseException exception) {
    if (exception.getStatusCode().is4xxClientError()) {
      return new InvalidInputException(CommonErrorCode.EXTERNAL_CLIENT_ERROR);
    }

    return exception;
  }
}
