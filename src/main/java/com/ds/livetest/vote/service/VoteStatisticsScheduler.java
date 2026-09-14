package com.ds.livetest.vote.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteStatisticsScheduler {

  private static final long TEN_MINUTES_MILLIS = 600_000L;

  private final VoteStatisticsService voteStatisticsService;

  @Scheduled(fixedDelay = TEN_MINUTES_MILLIS, initialDelay = TEN_MINUTES_MILLIS)
  public void refreshStatistics() {
    voteStatisticsService.refreshStatistics();
  }
}
