package com.ds.livetest.support.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FieldErrorResponse")
public record FieldErrorResponse(String field, String message) {}
