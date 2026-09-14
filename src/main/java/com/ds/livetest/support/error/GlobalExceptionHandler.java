package com.ds.livetest.support.error;

import com.ds.livetest.support.response.ApiResponse;
import com.ds.livetest.support.response.ErrorResponse;
import com.ds.livetest.support.response.FieldErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BusinessException.class)
  ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    logClientError(exception);
    return ResponseEntity.status(errorCode.status())
        .body(ApiResponse.error(ErrorResponse.from(errorCode)));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception) {
    List<FieldErrorResponse> fieldErrors =
        exception.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    new FieldErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();

    logClientError(exception);
    return errorResponse(CommonErrorCode.INVALID_INPUT, fieldErrors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
      ConstraintViolationException exception) {
    List<FieldErrorResponse> fieldErrors =
        exception.getConstraintViolations().stream()
            .map(
                violation ->
                    new FieldErrorResponse(
                        fieldName(violation.getPropertyPath().toString()), violation.getMessage()))
            .toList();

    logClientError(exception);
    return errorResponse(CommonErrorCode.INVALID_INPUT, fieldErrors);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidation(
      HandlerMethodValidationException exception) {
    List<FieldErrorResponse> fieldErrors =
        exception.getParameterValidationResults().stream()
            .flatMap(
                validationResult ->
                    validationResult.getResolvableErrors().stream()
                        .map(
                            resolvableError ->
                                new FieldErrorResponse(
                                    parameterName(validationResult),
                                    resolvableError(resolvableError))))
            .toList();

    logClientError(exception);
    return errorResponse(CommonErrorCode.INVALID_INPUT, fieldErrors);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException exception) {
    logClientError(exception);
    return errorResponse(
        CommonErrorCode.INVALID_INPUT,
        List.of(new FieldErrorResponse(exception.getName(), "must be a valid value")));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
      HttpMessageNotReadableException exception) {
    logClientError(exception);
    return errorResponse(CommonErrorCode.INVALID_INPUT);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameter(
      MissingServletRequestParameterException exception) {
    logClientError(exception);
    return errorResponse(
        CommonErrorCode.INVALID_INPUT,
        List.of(new FieldErrorResponse(exception.getParameterName(), "is required")));
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  ResponseEntity<ApiResponse<Void>> handleMissingRequestHeader(
      MissingRequestHeaderException exception) {
    logClientError(exception);
    return errorResponse(
        CommonErrorCode.INVALID_INPUT,
        List.of(new FieldErrorResponse(exception.getHeaderName(), "is required")));
  }

  @ExceptionHandler(MissingPathVariableException.class)
  ResponseEntity<ApiResponse<Void>> handleMissingPathVariable(
      MissingPathVariableException exception) {
    logClientError(exception);
    return errorResponse(
        CommonErrorCode.INVALID_INPUT,
        List.of(new FieldErrorResponse(exception.getVariableName(), "is required")));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException exception) {
    logClientError(exception);
    return errorResponse(CommonErrorCode.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  ResponseEntity<ApiResponse<Void>> handleMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException exception) {
    logClientError(exception);
    return errorResponse(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  ResponseEntity<ApiResponse<Void>> handleMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException exception) {
    logClientError(exception);
    return errorResponse(CommonErrorCode.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  ResponseEntity<ApiResponse<Void>> handleNoResourceFound(NoResourceFoundException exception) {
    logClientError(exception);
    return errorResponse(CommonErrorCode.NOT_FOUND_RESOURCE);
  }

  @ExceptionHandler(HttpClientErrorException.class)
  ResponseEntity<ApiResponse<Void>> handleHttpClientError(HttpClientErrorException exception) {
    logClientError(exception);
    return errorResponse(CommonErrorCode.EXTERNAL_CLIENT_ERROR);
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception exception) {
    log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
    return errorResponse(CommonErrorCode.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ApiResponse<Void>> errorResponse(ErrorCode errorCode) {
    return errorResponse(errorCode, List.of());
  }

  private ResponseEntity<ApiResponse<Void>> errorResponse(
      ErrorCode errorCode, List<FieldErrorResponse> fieldErrors) {
    return ResponseEntity.status(errorCode.status())
        .body(ApiResponse.error(ErrorResponse.from(errorCode, fieldErrors)));
  }

  private String parameterName(ParameterValidationResult validationResult) {
    MethodParameter methodParameter = validationResult.getMethodParameter();
    String parameterName = methodParameter.getParameterName();
    if (parameterName != null) {
      return parameterName;
    }
    return "parameter" + methodParameter.getParameterIndex();
  }

  private String fieldName(String propertyPath) {
    int lastDotIndex = propertyPath.lastIndexOf('.');
    if (lastDotIndex < 0 || lastDotIndex == propertyPath.length() - 1) {
      return propertyPath;
    }
    return propertyPath.substring(lastDotIndex + 1);
  }

  private String resolvableError(MessageSourceResolvable resolvableError) {
    String defaultMessage = resolvableError.getDefaultMessage();
    if (defaultMessage != null) {
      return defaultMessage;
    }
    return "is invalid";
  }

  private void logClientError(Exception exception) {
    log.warn("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
  }
}
