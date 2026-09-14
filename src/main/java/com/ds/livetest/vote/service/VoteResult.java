package com.ds.livetest.vote.service;

public record VoteResult(long jajang, long jjamppong) {

  public long total() {
    return jajang + jjamppong;
  }
}
