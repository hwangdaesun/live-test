package com.ds.livetest.vote.presentation;

import com.ds.livetest.vote.domain.Vote;
import java.util.UUID;

public record VoteResponse(UUID id, String choice, String voterId) {

  public static VoteResponse from(Vote vote) {
    return new VoteResponse(vote.getId(), vote.getChoice().value(), vote.getVoterId());
  }
}
