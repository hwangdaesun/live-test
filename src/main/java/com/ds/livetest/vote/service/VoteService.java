package com.ds.livetest.vote.service;

import com.ds.livetest.support.error.ConflictException;
import com.ds.livetest.vote.VoteErrorCode;
import com.ds.livetest.vote.domain.Vote;
import com.ds.livetest.vote.domain.VoteChoice;
import com.ds.livetest.vote.repository.VoteRepository;
import com.ds.livetest.vote.repository.VoteStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

  private final VoteRepository voteRepository;
  private final VoteStatisticRepository voteStatisticRepository;

  @Transactional
  public Vote createVote(String voterId, VoteChoice choice) {
    voteRepository
        .findByVoterId(voterId)
        .ifPresent(
            vote -> {
              throw new ConflictException(VoteErrorCode.DUPLICATE_VOTER);
            });

    try {
      Vote vote = voteRepository.saveAndFlush(Vote.create(voterId, choice));
      voteStatisticRepository.increase(choice.value());
      return vote;
    } catch (DataIntegrityViolationException exception) {
      throw new ConflictException(VoteErrorCode.DUPLICATE_VOTER);
    }
  }
}
