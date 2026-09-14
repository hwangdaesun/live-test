package com.ds.livetest.vote.service;

import com.ds.livetest.vote.domain.VoteChoice;
import com.ds.livetest.vote.domain.VoteStatistic;
import com.ds.livetest.vote.repository.VoteStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteStatisticsService {

  private final VoteStatisticRepository voteStatisticRepository;

  @Transactional(readOnly = true)
  public VoteResult getResult() {
    return new VoteResult(findVoteCount(VoteChoice.JAJANG), findVoteCount(VoteChoice.JJAMPPONG));
  }

  @Transactional
  public void incrementVoteCount(VoteChoice choice) {
    voteStatisticRepository.incrementCount(choice.value());
  }

  private long findVoteCount(VoteChoice choice) {
    return voteStatisticRepository
        .findById(choice.value())
        .map(VoteStatistic::getVoteCount)
        .orElse(0L);
  }
}
