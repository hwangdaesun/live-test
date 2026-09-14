package com.ds.livetest.support.external;

import static org.assertj.core.api.Assertions.assertThat;

import com.ds.livetest.support.error.CommonErrorCode;
import com.ds.livetest.support.error.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

class ExternalApiExceptionTranslatorTest {

  @Test
  void translateReturnsExternalClientErrorForHttpClientError() {
    RuntimeException translated =
        ExternalApiExceptionTranslator.translate(
            new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    assertThat(translated).isInstanceOf(InvalidInputException.class);
    assertThat(((InvalidInputException) translated).getErrorCode())
        .isEqualTo(CommonErrorCode.EXTERNAL_CLIENT_ERROR);
  }

  @Test
  void translateKeepsHttpServerErrorForResiliencePolicy() {
    HttpServerErrorException exception =
        new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    RuntimeException translated = ExternalApiExceptionTranslator.translate(exception);

    assertThat(translated).isSameAs(exception);
  }
}
