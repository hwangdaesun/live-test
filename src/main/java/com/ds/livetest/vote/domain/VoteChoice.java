package com.ds.livetest.vote.domain;

import java.util.Arrays;

public enum VoteChoice {
  JAJANG("jajang"),
  JJAMPPONG("jjamppong");

  private final String value;

  VoteChoice(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public static VoteChoice from(String value) {
    return Arrays.stream(values())
        .filter(choice -> choice.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unsupported vote choice: " + value));
  }
}
