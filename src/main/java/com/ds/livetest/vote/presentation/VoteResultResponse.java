package com.ds.livetest.vote.presentation;

import com.ds.livetest.vote.service.VoteResult;

public record VoteResultResponse(long jajang, long jjamppong, long total) {

  public static VoteResultResponse from(VoteResult voteResult) {
    return new VoteResultResponse(voteResult.jajang(), voteResult.jjamppong(), voteResult.total());
  }
}
