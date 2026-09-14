package com.ds.livetest.vote.presentation;

import com.ds.livetest.generated.api.VoteApi;
import com.ds.livetest.generated.dto.VoteRequest;
import com.ds.livetest.support.response.ApiEnvelope;
import com.ds.livetest.vote.domain.Vote;
import com.ds.livetest.vote.domain.VoteChoice;
import com.ds.livetest.vote.service.VoteResult;
import com.ds.livetest.vote.service.VoteService;
import com.ds.livetest.vote.service.VoteStatisticsService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoteController implements VoteApi {

  private final VoteService voteService;
  private final VoteStatisticsService voteStatisticsService;

  @Override
  public ResponseEntity<ApiEnvelope> createVote(VoteRequest voteRequest) {
    Vote vote =
        voteService.createVote(
            voteRequest.getVoterId(), VoteChoice.from(voteRequest.getChoice().getValue()));

    return ResponseEntity.created(URI.create("/api/vote/" + vote.getId()))
        .body(ApiEnvelope.ofSuccess(VoteResponse.from(vote)));
  }

  @Override
  public ResponseEntity<ApiEnvelope> getVoteResult() {
    VoteResult result = voteStatisticsService.getResult();
    return ResponseEntity.ok(ApiEnvelope.ofSuccess(VoteResultResponse.from(result)));
  }
}
